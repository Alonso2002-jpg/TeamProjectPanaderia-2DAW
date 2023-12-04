package org.develop.TeamProjectPanaderia.rest.cliente.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.criteria.Join;
import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.WebSockets.dto.NotificacionResponseDto;
import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.WebSockets.model.Notificacion;
import org.develop.TeamProjectPanaderia.rest.categoria.exceptions.CategoriaNotFoundException;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteCreateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.dto.ClienteUpdateDto;
import org.develop.TeamProjectPanaderia.rest.cliente.exceptions.ClienteBadRequest;
import org.develop.TeamProjectPanaderia.rest.cliente.exceptions.ClienteNotFoundException;
import org.develop.TeamProjectPanaderia.rest.cliente.exceptions.ClienteNotSaveException;
import org.develop.TeamProjectPanaderia.rest.cliente.mapper.ClienteMapper;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Cliente;
import org.develop.TeamProjectPanaderia.rest.cliente.models.Direccion;
import org.develop.TeamProjectPanaderia.rest.cliente.repositories.ClienteRepository;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketConfig;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketHandler;
import org.develop.TeamProjectPanaderia.storage.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Cliente.
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "clientes")
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository clienteRepository;
    private final CategoriaService categoriaService;
    private final ClienteMapper clienteMapper;
    private final StorageService storageService;
    private final WebSocketConfig webSocketConfig;
    private final ObjectMapper mapper;
    private final NotificacionMapper<Cliente> clienteNotificacionMapper;
    private WebSocketHandler webSocketService;

    /**
     * Constructor de la clase ClienteServiceImpl.
     *
     * @param clienteRepository        Repositorio de clientes.
     * @param categoriaService         Servicio de categoría.
     * @param clienteMapper            Mapper para la conversión entre entidades y DTOs de clientes.
     * @param storageService           Servicio de almacenamiento.
     * @param webSocketConfig          Configuración del WebSocket.
     * @param mapper                   Objeto ObjectMapper para la conversión de objetos a JSON.
     * @param clienteNotificacionMapper Mapper para la conversión de clientes a notificaciones.
     */
    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository, CategoriaService categoriaService, ClienteMapper clienteMapper, StorageService storageService, WebSocketConfig webSocketConfig, ObjectMapper mapper, NotificacionMapper<Cliente> clienteNotificacionMapper) {
        this.clienteRepository = clienteRepository;
        this.categoriaService = categoriaService;
        this.clienteMapper = clienteMapper;
        this.storageService = storageService;
        this.webSocketConfig = webSocketConfig;
        webSocketConfig.setUrlAndEntity("cliente","Cliente");
        webSocketService = webSocketConfig.webSocketHandler();
        this.mapper = mapper;
        this.clienteNotificacionMapper = clienteNotificacionMapper;
    }

    /**
     * Obtiene una página de clientes según los criterios proporcionados.
     *
     * @param nombreCompleto Nombre completo del cliente (opcional).
     * @param categoria      Categoría del cliente (opcional).
     * @param pageable       Objeto Pageable para la paginación y ordenación de resultados.
     * @return Una página de clientes que cumple con los criterios especificados.
     */
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

    /**
     * Busca un cliente por su identificador único (ID).
     *
     * @param id El ID del cliente a buscar.
     * @return El cliente encontrado.
     * @throws ClienteNotFoundException Si no se encuentra el cliente con el ID especificado.
     */
    @Override
    @Cacheable
    public Cliente findById(Long id) {
        log.info("Buscando cliente por id: " + id);
        return clienteRepository.findById(id).orElseThrow(() -> new ClienteNotFoundException(id));
    }

    /**
     * Guarda un nuevo cliente utilizando la información proporcionada en el objeto ClienteCreateDto.
     *
     * @param clienteCreateDto Objeto DTO con la información del cliente a guardar.
     * @return El cliente guardado.
     * @throws ClienteNotSaveException Si ya existe un cliente con el mismo DNI.
     * @throws CategoriaNotFoundException Si la categoría especificada no existe.
     */
    @Override
    @CachePut
    public Cliente save(ClienteCreateDto clienteCreateDto) {
        log.info("Guardando cliente: " + clienteCreateDto);
        try{
            if(clienteRepository.findClienteByDniEqualsIgnoreCase(clienteCreateDto.getDni()).isPresent()){
                throw new ClienteNotSaveException(clienteCreateDto.getDni());
            }
            Categoria categoria = categoriaService.findByName(clienteCreateDto.getCategoria());
            Cliente clienteSaved = clienteRepository.save(clienteMapper.toCliente(clienteCreateDto, categoria));
            onChange(Notificacion.Tipo.CREATE, clienteSaved);
            return clienteSaved;
        } catch (CategoriaNotFoundException e){
            throw new ClienteBadRequest(clienteCreateDto.getCategoria());
        }
    }

    /**
     * Actualiza la información de un cliente existente utilizando el objeto ClienteUpdateDto.
     *
     * @param id                El identificador único del cliente a actualizar.
     * @param clienteUpdateDto Objeto DTO con la información actualizada del cliente.
     * @return El cliente actualizado.
     * @throws ClienteNotFoundException Si no se encuentra el cliente con el ID especificado.
     * @throws CategoriaNotFoundException Si la categoría especificada no existe.
     */
    @Override
    @CachePut
    public Cliente update(Long id, ClienteUpdateDto clienteUpdateDto) {
        log.info("Actualizando cliente por id: " + id);
        try{
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
        } catch (CategoriaNotFoundException e){
            throw new ClienteBadRequest(clienteUpdateDto.getCategoria());
        }
    }

    /**
     * Actualiza la dirección de un cliente existente utilizando el objeto Direccion.
     *
     * @param id       El identificador único del cliente a actualizar.
     * @param direccion Objeto Direccion con la nueva información de la dirección.
     * @return El cliente actualizado.
     * @throws ClienteNotFoundException Si no se encuentra el cliente con el ID especificado.
     */
    @Override
    @CachePut
    public Cliente updateDireccion(Long id, Direccion direccion){
        log.info("Actualizando direccion de cliente por id: " + id);
        Cliente clienteActual = this.findById(id);
        clienteActual.setDireccion(clienteMapper.toJson(direccion));
        Cliente clienteUpdated = clienteRepository.save(clienteActual);
        onChange(Notificacion.Tipo.UPDATE, clienteUpdated);
        return clienteUpdated;
    }

    /**
     * Obtiene la categoría actualizada para un cliente, tomando en cuenta el valor en el ClienteUpdateDto
     * y utilizando la categoría actual si no se proporciona un valor.
     *
     * @return La categoría actualizada para el cliente.
     * @throws CategoriaNotFoundException Si la categoría especificada no existe.
     */
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

    /**
     * Busca y devuelve un cliente por su número de DNI.
     *
     * @param dni Número de DNI del cliente a buscar.
     * @return El cliente encontrado.
     * @throws ClienteNotFoundException Si no se encuentra un cliente con el DNI especificado.
     */
    @Override
    @Cacheable
    public Cliente findByDni(String dni) {
        log.info("Buscando cliente por dni: " + dni);
        return clienteRepository.findClienteByDniEqualsIgnoreCase(dni).orElseThrow(() -> new ClienteNotFoundException(dni));
    }

    /**
     * Elimina un cliente por su identificador único.
     *
     * @param id Identificador único del cliente a eliminar.
     * @throws ClienteNotFoundException Si no se encuentra el cliente con el ID especificado.
     */
    @Override
    @CacheEvict
    public void deleteById(Long id) {
        log.debug("Borrando cliente por id: " + id);
        Cliente clienteActual = this.findById(id);
        clienteRepository.deleteById(clienteActual.getId());

        if (clienteActual.getImagen() != null && !clienteActual.getImagen().equals(Cliente.IMAGE_DEFAULT)){
            storageService.delete(clienteActual.getImagen());
        }
        onChange(Notificacion.Tipo.DELETE, clienteActual);
    }

    /**
     * Método invocado para notificar cambios en los clientes a través de WebSocket.
     *
     * @param tipo Tipo de cambio (CREATE, UPDATE, DELETE).
     * @param data Datos relacionados al cambio (cliente).
     */
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

    /**
     * Establece el servicio WebSocket. Útil para pruebas unitarias.
     *
     * @param webSocketHandlerMock Mock del servicio WebSocket.
     */
    public void setWebSocketService(WebSocketHandler webSocketHandlerMock) {
        this.webSocketService = webSocketHandlerMock;
    }

    /**
     * Busca y devuelve una lista de clientes activos o inactivos según el parámetro.
     *
     * @param isActive Indica si se buscan clientes activos (true) o inactivos (false).
     * @return Lista de clientes según el estado de activación especificado.
     */
    @Override
    public List<Cliente> findByActiveIs(Boolean isActive) {
        return clienteRepository.findByIsActive(isActive);
    }
}
