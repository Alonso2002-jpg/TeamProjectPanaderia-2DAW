package org.develop.TeamProjectPanaderia.categoria.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.develop.TeamProjectPanaderia.WebSockets.dto.NotificacionResponseDto;
import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.WebSockets.model.Notificacion;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaCreateDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaResponseDto;
import org.develop.TeamProjectPanaderia.categoria.dto.CategoriaUpdateDto;
import org.develop.TeamProjectPanaderia.categoria.exceptions.CategoriaNotFoundException;
import org.develop.TeamProjectPanaderia.categoria.exceptions.CategoriaNotSaveException;
import org.develop.TeamProjectPanaderia.categoria.mapper.CategoriaMapper;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.repositories.CategoriaRepository;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketConfig;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class CategoriaServiceImpl implements CategoriaService{
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final WebSocketConfig webSocketConfig;
    private WebSocketHandler webSocketHandler;
    private final ObjectMapper objMapper;
    private final NotificacionMapper<CategoriaResponseDto> notificacionMapper;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository,
                                CategoriaMapper categoriaMapper,
                                WebSocketConfig webSocketConfig,
                                WebSocketHandler webSocketHandler,
                                ObjectMapper objMapper,
                                NotificacionMapper notificacionMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
        this.webSocketConfig = webSocketConfig;
        this.webSocketHandler = webSocketHandler;
        this.objMapper = objMapper;
        this.notificacionMapper = notificacionMapper;
    }

    @Override
    public List<Categoria> findAll(Boolean isActive) {
        return isActive != null ? categoriaRepository.findByIsActive(isActive) : categoriaRepository.findAll();
    }

    @Override
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new CategoriaNotFoundException("id " + id));
    }

    @Override
    public Categoria save(CategoriaCreateDto categoria) {
        if (categoriaRepository.findByNameCategoryIgnoreCase(categoria.nameCategory()).isPresent()){
            throw new CategoriaNotSaveException("Category already exists");
        }
        var category = categoriaMapper.toCategoria(categoria);
        onChange(Notificacion.Tipo.CREATE, category);
        return categoriaRepository.save(category);
    }

    @Override
    public Categoria update(Long id,CategoriaUpdateDto categoria) {
        var categoriaUpd = findById(id);
        var category = categoriaMapper.toCategoria(categoria,categoriaUpd);
        onChange(Notificacion.Tipo.UPDATE, category);
        return categoriaRepository.save(category);
    }

    @Override
    public Categoria findByName(String name) {
        return categoriaRepository.findByNameCategoryIgnoreCase(name).orElseThrow(() -> new CategoriaNotFoundException("name " + name));
    }

    @Override
    public List<Categoria> findByActiveIs(boolean isActive) {
        return categoriaRepository.findByIsActive(isActive);
    }

    @Override
    public void deleteById(Long id) {
        var category = findById(id);
        onChange(Notificacion.Tipo.DELETE, category);
        categoriaRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        categoriaRepository.deleteAll();
    }

    void onChange(Notificacion.Tipo tipo, Categoria data){
        log.debug("Servicio de productos onChange con tipo: " + tipo + " y datos: " + data);

        if (webSocketHandler == null){
            log.warn("No se ha podido enviar la Notificacion, no se encontro servicio");
            webSocketHandler = this.webSocketConfig.webSocketHandler();
        }
        try{
            Notificacion<NotificacionResponseDto> notificacion = new Notificacion<>(
                    "Categoria",
                    tipo,
                    notificacionMapper.getNotificacionResponseDto(categoriaMapper.toResponse(data),"Categoria"),
                    LocalDate.now().toString()
            );

            String json = objMapper.writeValueAsString((notificacion));

            log.info("Enviando Notificacion a los Clientes WS");

            Thread senderThread = new Thread(()->{
                try{
                    webSocketHandler.sendMessage(json);
                } catch (IOException e) {
                    log.error("Error Enviando el Mensaje atraves del WS", e);
                }
            });

            senderThread.start();
        } catch (JsonProcessingException e) {
             log.error("Error al convertir la notificación a JSON", e);
        }
    }
}
