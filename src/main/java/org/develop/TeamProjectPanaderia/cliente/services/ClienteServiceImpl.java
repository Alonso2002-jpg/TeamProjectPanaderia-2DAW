package org.develop.TeamProjectPanaderia.cliente.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Join;
import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.WebSockets.dto.NotificacionResponseDto;
import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.WebSockets.model.Notificacion;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.cliente.exceptions.ClienteNotFoundException;
import org.develop.TeamProjectPanaderia.cliente.exceptions.ClienteNotSaveException;
import org.develop.TeamProjectPanaderia.cliente.mapper.ClienteMapper;
import org.develop.TeamProjectPanaderia.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.cliente.repositories.ClienteRepository;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketConfig;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketHandler;
import org.develop.TeamProjectPanaderia.storage.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@Slf4j
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository clienteRepository;
    private final CategoriaService categoriaService;
    private final ClienteMapper clienteMapper;
    private final StorageService storageService;
    private final WebSocketConfig webSocketConfig;
    private final ObjectMapper mapper;
    private final NotificacionMapper<Cliente> clienteNotificacionMapper;
    private WebSocketHandler webSocketService;

    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository, CategoriaService categoriaService, ClienteMapper clienteMapper, StorageService storageService, WebSocketConfig webSocketConfig, ObjectMapper mapper, NotificacionMapper<Cliente> clienteNotificacionMapper) {
        this.clienteRepository = clienteRepository;
        this.categoriaService = categoriaService;
        this.clienteMapper = clienteMapper;
        this.storageService = storageService;
        this.webSocketConfig = webSocketConfig;
        webSocketService = webSocketConfig.webSocketHandler();
        this.mapper = mapper;
        this.clienteNotificacionMapper = clienteNotificacionMapper;
    }


    @Override
    public Page<Cliente> findAll(Optional<String> nombreCompleto, Optional<String> categoria, Pageable pageable){

        // Criteerio de búsqueda por nombreCompleto
        Specification<Cliente> specNombreCompleto = (root, query, criteriaBuilder) ->
                nombreCompleto.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombreCompleto")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));


        // Criterio de busqueda por categoria
        Specification<Cliente> specCategoria = (root, query, criteriaBuilder) ->
                categoria.map(c ->{
                    Join<Cliente, Categoria> categoriaJoin = root.join("categoria");
                    return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("nameCategory")), "%" + c.toLowerCase() + "%");
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Cliente> criterio = Specification.where(specNombreCompleto)
                 .and(specCategoria);
         return clienteRepository.findAll(criterio, pageable);
    }

    @Override
    public Cliente findById(Long id) {
        log.info("Buscando cliente por id: " + id);
        return clienteRepository.findById(id).orElseThrow(() -> new ClienteNotFoundException(id));
    }

    @Override
    public Cliente save(ClienteCreateDto clienteCreateDto) {
        log.info("Guardando cliente: " + clienteCreateDto);
        if(clienteRepository.findClienteByDniEqualsIgnoreCase(clienteCreateDto.getDni()).isPresent()){
            throw new ClienteNotSaveException(clienteCreateDto.getDni());
        }
        Categoria categoria = categoriaService.findByName(clienteCreateDto.getCategoria());
        Cliente clienteSaved = clienteRepository.save(clienteMapper.toCliente(clienteCreateDto, categoria));
        onChange(Notificacion.Tipo.CREATE, clienteSaved);
        return clienteSaved;
    }

    @Override
    public Cliente update(Long id, ClienteUpdateDto clienteUpdateDto) {
        log.info("Actualizando cliente por id: " + id);
        Cliente clienteActual = this.findById(id);
        Categoria categoria = null;
        if(clienteUpdateDto.getCategoria() != null && !clienteUpdateDto.getCategoria().isEmpty()){
            categoria = categoriaService.findByName(clienteUpdateDto.getCategoria());
        } else {
            categoria = clienteActual.getCategoria();
        }
        Cliente clienteMapped = clienteMapper.toCliente(clienteUpdateDto, clienteActual, categoria);
        Cliente clienteUpdated = clienteRepository.save(clienteMapped);
        onChange(Notificacion.Tipo.UPDATE, clienteUpdated);
        return clienteUpdated;
    }

    @Override
    @CachePut
    public Cliente updateImg(Long id, MultipartFile file){
        log.info("Actualizando imagen de client por id: " + id);
        Cliente clienteActual = this.findById(id);
        String img = storageService.store(file);
        String urlImg = storageService.getUrl(img).replace(" ", "");
        if (!clienteActual.getImagen().equals(Cliente.IMAGE_DEFAULT)){
            storageService.delete(clienteActual.getImagen());
        }
        clienteActual.setImagen(urlImg);
        Cliente clienteUpdated = clienteRepository.save(clienteActual);
        onChange(Notificacion.Tipo.UPDATE, clienteUpdated);
        return clienteUpdated;
    }

    @Override
    public Cliente findByDni(String dni) {
        log.info("Buscando cliente por dni: " + dni);
        return clienteRepository.findClienteByDniEqualsIgnoreCase(dni).orElseThrow(() -> new ClienteNotFoundException(dni));
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Borrando cliente por id: " + id);
        Cliente clienteActual = this.findById(id);
        clienteRepository.deleteById(clienteActual.getId());

        if (clienteActual.getImagen() != null && !clienteActual.getImagen().equals(Cliente.IMAGE_DEFAULT)){
            storageService.delete(clienteActual.getImagen());
        }
        onChange(Notificacion.Tipo.DELETE, clienteActual);
    }


    void onChange(Notificacion.Tipo tipo, Cliente data) {
        log.debug("Servicio de Clientes onChange con tipo: " + tipo + " y datos: " + data);

        if (webSocketService == null) {
            log.warn("No se ha podido enviar la notificación a los clientes ws, no se ha encontrado el servicio");
            webSocketService = this.webSocketConfig.webSocketHandler();
        }

        try {
            Notificacion<NotificacionResponseDto> notificacion = new Notificacion<>(
                    "CLIENTES",
                    tipo,
                    clienteNotificacionMapper.getNotificacionResponseDto(data, "CLIENTES"),
                    LocalDateTime.now().toString()
            );

            String json = mapper.writeValueAsString((notificacion));

            log.info("Enviando mensaje a los clientes ws");

            Thread senderThread = new Thread(() -> {
                try {
                    webSocketService.sendMessage(json);
                } catch (Exception e) {
                    log.error("Error al enviar el mensaje a través del servicio WebSocket", e);
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            log.error("Error al convertir la notificación a JSON", e);
        }
    }

    public void setWebSocketService(WebSocketHandler webSocketHandlerMock) {
        this.webSocketService = webSocketHandlerMock;
    }
}
