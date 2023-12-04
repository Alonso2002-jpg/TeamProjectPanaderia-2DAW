package org.develop.TeamProjectPanaderia.rest.categoria.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.develop.TeamProjectPanaderia.WebSockets.dto.NotificacionResponseDto;
import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.WebSockets.model.Notificacion;
import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaCreateDto;
import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaResponseDto;
import org.develop.TeamProjectPanaderia.rest.categoria.dto.CategoriaUpdateDto;
import org.develop.TeamProjectPanaderia.rest.categoria.exceptions.CategoriaNotDeleteException;
import org.develop.TeamProjectPanaderia.rest.categoria.exceptions.CategoriaNotFoundException;
import org.develop.TeamProjectPanaderia.rest.categoria.exceptions.CategoriaNotSaveException;
import org.develop.TeamProjectPanaderia.rest.categoria.mapper.CategoriaMapper;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.categoria.repositories.CategoriaRepository;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketConfig;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketHandler;
import org.develop.TeamProjectPanaderia.rest.cliente.repositories.ClienteRepository;
import org.develop.TeamProjectPanaderia.rest.personal.repositories.PersonalRepository;
import org.develop.TeamProjectPanaderia.rest.producto.repositories.ProductoRepository;
import org.develop.TeamProjectPanaderia.rest.proveedores.repositories.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * Implementación de la interfaz {@link CategoriaService} que gestiona las operaciones relacionadas con la entidad {@link Categoria}.
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "categorias")
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
                                ObjectMapper objMapper,
                                NotificacionMapper notificacionMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;

        //Notificaciones
        this.webSocketConfig = webSocketConfig;
        webSocketConfig.setUrlAndEntity("categoria","Categoria");
        this.webSocketHandler = webSocketConfig.webSocketHandler();
        this.objMapper = objMapper;
        this.notificacionMapper = notificacionMapper;
    }
    /**
     * Obtiene todas las categorías que cumplen con los criterios dados.
     *
     * @param isActive Indica si la categoría está activa o no (opcional).
     * @param name     Nombre de la categoría (opcional).
     * @param pageable Paginación para la lista de categorías.
     * @return Página de categorías que cumplen con los criterios dados.
     */
    @Override
    public Page<Categoria> findAll(Optional<Boolean> isActive, Optional<String> name, Pageable pageable) {
        Specification<Categoria> findIsActive = (root, query, criteriaBuilder) ->
        isActive.map(isAc -> criteriaBuilder.equal(root.get("isActive"), isAc))
                .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Categoria> findName = (root, query, criteriaBuilder) ->
                name.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nameCategory")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Categoria> criterio = Specification.where(findIsActive).and(findName);

        return categoriaRepository.findAll(criterio, pageable);
    }
    /**
     * Obtiene una categoría por su identificador único.
     *
     * @param id Identificador único de la categoría.
     * @return La categoría encontrada.
     * @throws CategoriaNotFoundException Si la categoría no se encuentra.
     */
    @Override
    @Cacheable
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id).orElseThrow(() -> new CategoriaNotFoundException("id " + id));
    }
    /**
     * Guarda una nueva categoría en la base de datos.
     *
     * @param categoria La categoría a guardar.
     * @return La categoría guardada.
     * @throws CategoriaNotSaveException Si la categoría ya existe.
     */
    @Override
    @CachePut
    public Categoria save(CategoriaCreateDto categoria) {
        if (categoriaRepository.findByNameCategoryIgnoreCase(categoria.nameCategory()).isPresent()){
            throw new CategoriaNotSaveException("Category already exists");
        }
        var category = categoriaMapper.toCategoria(categoria);
        onChange(Notificacion.Tipo.CREATE, category);
        return categoriaRepository.save(category);
    }

    /**
     * Actualiza una categoría existente en la base de datos.
     *
     * @param id        Identificador único de la categoría a actualizar.
     * @param categoria La información actualizada de la categoría.
     * @return La categoría actualizada.
     * @throws CategoriaNotSaveException   Si ya existe otra categoría con el mismo nombre.
     * @throws CategoriaNotFoundException Si la categoría no se encuentra.
     */
    @Override
    @CachePut
    public Categoria update(Long id, CategoriaUpdateDto categoria) {
        var categoriaUpd = findById(id);
        if (!categoriaUpd.getNameCategory().equalsIgnoreCase(categoria.nameCategory()) && categoriaRepository.findByNameCategoryIgnoreCase(categoria.nameCategory()).isPresent()){
            throw new CategoriaNotSaveException("Category with this name already exists");
        }
        var category = categoriaMapper.toCategoria(categoria,categoriaUpd);
        onChange(Notificacion.Tipo.UPDATE, category);
        return categoriaRepository.save(category);
    }
    /**
     * Encuentra una categoría por su nombre.
     *
     * @param name Nombre de la categoría a buscar.
     * @return La categoría encontrada.
     * @throws CategoriaNotFoundException Si la categoría no se encuentra.
     */
    @Override
    public Categoria findByName(String name) {
        return categoriaRepository.findByNameCategoryIgnoreCase(name).orElseThrow(() -> new CategoriaNotFoundException("name " + name));
    }
    /**
     * Encuentra todas las categorías que estén activas o inactivas según el parámetro proporcionado.
     *
     * @param isActive Indica si las categorías a buscar deben estar activas o inactivas.
     * @return Lista de categorías que cumplen con el criterio proporcionado.
     */
    @Override
    public List<Categoria> findByActiveIs(boolean isActive) {
        return categoriaRepository.findByIsActive(isActive);
    }

    /**
     * Verifica si una categoría está asociada a algún elemento (Proveedor, Producto, Cliente o Personal).
     *
     * @param id Identificador único de la categoría a verificar.
     * @throws CategoriaNotDeleteException Si la categoría está asociada a algún elemento y no se puede eliminar.
     */
    @Override
    public void categoryExistsSomewhere(Long id) {
        if (categoriaRepository.existsProveedorByID(id)){
            throw new CategoriaNotDeleteException("Categoria cant be deleted because it has Proveedores");
        } else if (categoriaRepository.existsProductoById(id)) {
            throw new CategoriaNotDeleteException("Categoria cant be deleted because it has Productos");
        }else if (categoriaRepository.existsClienteById(id)){
            throw new CategoriaNotDeleteException("Categoria cant be deleted because it has Clientes");
        } else if (categoriaRepository.existsPersonalById(id)) {
            throw new CategoriaNotDeleteException("Categoria cant be deleted because it has Personals");
        }
    }
    /**
     * Elimina una categoría por su identificador único.
     *
     * @param id Identificador único de la categoría a eliminar.
     * @throws CategoriaNotFoundException Si la categoría no se encuentra.
     * @throws CategoriaNotDeleteException Si la categoría está asociada a algún elemento y no se puede eliminar.
     */
    @Override
    @CacheEvict
    public void deleteById(Long id) {
        var category = findById(id);
        categoryExistsSomewhere(id);
        onChange(Notificacion.Tipo.DELETE, category);
        categoriaRepository.deleteById(id);
    }

    /**
     * Elimina todas las categorías de la base de datos.
     */
    @Override
    public void deleteAll() {
        categoriaRepository.deleteAll();
    }
    /**
     * Envía una notificación de cambio a través de WebSocket.
     *
     * @param tipo Tipo de cambio (CREATE, UPDATE, DELETE).
     * @param data Datos de la categoría afectada.
     */
    public void onChange(Notificacion.Tipo tipo, Categoria data){
        log.debug("Servicio de productos onChange con tipo: " + tipo + " y datos: " + data);

        if (webSocketHandler == null){
            log.warn("No se ha podido enviar la Notificacion, no se encontro servicio");
           webSocketHandler = webSocketConfig.webSocketHandler();
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
