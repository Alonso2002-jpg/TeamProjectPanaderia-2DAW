package org.develop.TeamProjectPanaderia.rest.producto.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Join;
import lombok.extern.slf4j.Slf4j;

import org.develop.TeamProjectPanaderia.WebSockets.dto.NotificacionResponseDto;
import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.WebSockets.model.Notificacion;
import org.develop.TeamProjectPanaderia.rest.categoria.exceptions.CategoriaNotFoundException;
import org.develop.TeamProjectPanaderia.rest.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.rest.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketConfig;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketHandler;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.rest.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.rest.producto.exceptions.ProductoBadRequest;
import org.develop.TeamProjectPanaderia.rest.producto.exceptions.ProductoBadUuid;
import org.develop.TeamProjectPanaderia.rest.producto.exceptions.ProductoNotFound;
import org.develop.TeamProjectPanaderia.rest.producto.exceptions.ProductoNotSaved;
import org.develop.TeamProjectPanaderia.rest.producto.mapper.ProductoMapper;
import org.develop.TeamProjectPanaderia.rest.producto.models.Producto;
import org.develop.TeamProjectPanaderia.rest.producto.repositories.ProductoRepository;
import org.develop.TeamProjectPanaderia.rest.proveedores.exceptions.ProveedorNotFoundException;
import org.develop.TeamProjectPanaderia.rest.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.rest.proveedores.services.ProveedorService;
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
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación del servicio {@code ProductoService} que proporciona operaciones relacionadas con productos.
 *
 * @version 1.0
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "productos")
public class ProductoServiceImpl implements ProductoService{
    private final ProductoRepository productoRepository;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedoresService;
    private final ProductoMapper productoMapper;
    private final StorageService storageService;

    private final WebSocketConfig webSocketConfig;
    private final ObjectMapper mapper;
    private final NotificacionMapper<Producto> productoNotificacionMapper;
    private WebSocketHandler webSocketService;

    @Autowired
    public ProductoServiceImpl(
            ProductoRepository productoRepository,
            CategoriaService categoriaService,
            ProveedorService proveedoresService,
            ProductoMapper productoMapper,
            StorageService storageService, WebSocketConfig webSocketConfig, ObjectMapper mapper, NotificacionMapper<Producto> productoNotificacionMapper) {
        this.productoRepository = productoRepository;
        this.categoriaService = categoriaService;
        this.proveedoresService = proveedoresService;
        this.productoMapper = productoMapper;
        this.storageService = storageService;
        this.webSocketConfig = webSocketConfig;
        webSocketConfig.setUrlAndEntity("producto", "Producto");
        webSocketService = webSocketConfig.webSocketHandler();
        this.mapper = mapper;
        this.productoNotificacionMapper = productoNotificacionMapper;
    }

    /**
     * Recupera una página de productos según los criterios de búsqueda proporcionados.
     *
     * @param nombre    Nombre del producto (opcional).
     * @param stockMin  Cantidad mínima del producto (opcional).
     * @param precioMax Precio máximo del producto (opcional).
     * @param isActivo  Indica si el producto está activo (opcional).
     * @param categoria Nombre de la categoría del producto (opcional).
     * @param proveedor NIF del proveedor del producto (opcional).
     * @param pageable  Información de paginación y ordenación.
     * @return Una página de productos que cumplen con los criterios de búsqueda.
     */
    @Override
    public Page<Producto> findAll(Optional<String> nombre, Optional<Integer> stockMin, Optional<Double> precioMax, Optional<Boolean> isActivo, Optional<String> categoria, Optional<String> proveedor, Pageable pageable) {
        // Criteerio de búsqueda por nombre
        Specification<Producto> specNombreProducto = (root, query, criteriaBuilder) ->
                nombre.map(n -> criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + n.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de busqueda por stock
        Specification<Producto> specStockMinProducto = (root, query, criteriaBuilder) ->
                stockMin.map(c -> criteriaBuilder.greaterThanOrEqualTo(root.get("stock"),c))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de busqueda por precio
        Specification<Producto> specPrecioMaxProducto = (root, query, criteriaBuilder) ->
                precioMax.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("precio"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de busqueda por isActivo
        Specification<Producto> specIsActivo = (root, query, criteriaBuilder) ->
                isActivo.map(a -> criteriaBuilder.equal(root.get("isActivo"), a))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de busqueda por categoria
        Specification<Producto> specCategoriaProducto = (root, query, criteriaBuilder) ->
                categoria.map(c ->{
                    Join<Producto, Categoria> categoriaJoin = root.join("categoria");
                    return criteriaBuilder.like(criteriaBuilder.lower(categoriaJoin.get("nameCategory")), "%" + c.toLowerCase() + "%");
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de busqueda por proveedor
        Specification<Producto> specProveedorProducto = (root, query, criteriaBuilder) ->
                proveedor.map(c ->{
                    Join<Producto, Proveedor> proveedorJoin = root.join("proveedor");
                    return criteriaBuilder.like(criteriaBuilder.lower(proveedorJoin.get("nif")), "%" + c.toLowerCase() + "%");
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Producto> criterio = Specification.where(specNombreProducto)
                .and(specStockMinProducto)
                .and(specPrecioMaxProducto)
                .and(specIsActivo)
                .and(specCategoriaProducto)
                .and(specProveedorProducto);
        return productoRepository.findAll(criterio, pageable);
    }

    /**
     * Recupera un producto por su identificador único.
     *
     * @param id Identificador único del producto.
     * @return El producto con el identificador dado.
     * @throws ProductoNotFound Si no se encuentra un producto con el identificador proporcionado.
     * @throws ProductoBadUuid  Si el identificador proporcionado no es un UUID válido.
     */
    @Override
    @Cacheable
    public Producto findById(String id) {
        log.info("Buscando producto por id: " + id);
        try{
            var uuid = UUID.fromString(id);
            return productoRepository.findById(uuid).orElseThrow(() -> new ProductoNotFound(uuid));
        } catch (IllegalArgumentException e) {
                throw new ProductoBadUuid(id);
        }
    }

    /**
     * Recupera un producto por su nombre, ignorando mayúsculas y minúsculas.
     *
     * @param name Nombre del producto.
     * @return El producto con el nombre dado.
     * @throws ProductoNotFound Si no se encuentra un producto con el nombre proporcionado.
     */
    @Override
    @Cacheable
    public Producto findByName(String name) {
        log.info("Buscando producto por nombre: " + name);
        return productoRepository.findByNombreEqualsIgnoreCase(name).orElseThrow(() -> new ProductoNotFound(name));
    }

    /**
     * Guarda un nuevo producto en la base de datos.
     *
     * @param productoCreateDto Datos del producto a crear.
     * @return El producto creado.
     * @throws ProductoNotSaved     Si ya existe un producto con el mismo nombre en la base de datos.
     * @throws CategoriaNotFoundException Si la categoría especificada no existe.
     * @throws ProveedorNotFoundException  Si el proveedor especificado no existe.
     * @throws ProductoBadRequest   Si la categoría o el proveedor no son válidos.
     */
    @Override
    @CachePut
    public Producto save(ProductoCreateDto productoCreateDto) {
        log.info("Guardando producto: " + productoCreateDto);
        try{
            if(productoRepository.findByNombreEqualsIgnoreCase(productoCreateDto.nombre()).isPresent()) {
                throw new ProductoNotSaved(productoCreateDto.nombre());
            }
            Categoria categoria = categoriaService.findByName(productoCreateDto.categoria());
            Proveedor proveedor = proveedoresService.findProveedoresByNIF(productoCreateDto.proveedor());
            UUID id = UUID.randomUUID();
            Producto productoMapped = productoMapper.toProducto(id,productoCreateDto, categoria, proveedor);
            Producto productoSaved = productoRepository.save(productoMapped);
            onChange(Notificacion.Tipo.CREATE, productoSaved);
            return productoSaved;
        } catch (CategoriaNotFoundException e){
            throw new ProductoBadRequest("La categoria con nombre " + productoCreateDto.categoria() + " no existe");
        } catch (ProveedorNotFoundException e){
            throw new ProductoBadRequest("El proveedor con nif " + productoCreateDto.proveedor() + " no existe");
        }
    }

    /**
     * Actualiza un producto existente en la base de datos.
     *
     * @param id                Identificador único del producto a actualizar.
     * @param productoUpdateDto Datos actualizados del producto.
     * @return El producto actualizado.
     * @throws ProductoNotFound          Si no se encuentra un producto con el identificador proporcionado.
     * @throws ProductoNotSaved          Si ya existe un producto con el mismo nombre en la base de datos.
     * @throws CategoriaNotFoundException Si la categoría especificada no existe.
     * @throws ProveedorNotFoundException  Si el proveedor especificado no existe.
     * @throws ProductoBadRequest        Si la categoría o el proveedor no son válidos.
     */
    @Override
    @CachePut
    public Producto update(String id, ProductoUpdateDto productoUpdateDto) {
       log.info("Actualizando producto por id: " + id);
       try{
           Producto productoActual = this.findById(id);
           if(productoUpdateDto.nombre() != null && !productoUpdateDto.nombre().isEmpty()) {
               Optional <Producto> productoSameName = productoRepository.findByNombreEqualsIgnoreCase(productoUpdateDto.nombre());
               if(productoSameName.isPresent() && productoSameName.get().getId() != productoActual.getId()) {
                   throw new ProductoNotSaved(productoUpdateDto.nombre());
               }
           }
           Categoria categoria = null;
           Proveedor proveedor = null;
           if(productoUpdateDto.categoria() != null && !productoUpdateDto.categoria().isEmpty()){
               categoria = categoriaService.findByName(productoUpdateDto.categoria());
           } else {
               categoria = productoActual.getCategoria();
           }
           if(productoUpdateDto.proveedor() != null && !productoUpdateDto.proveedor().isEmpty()){
               proveedor = proveedoresService.findProveedoresByNIF(productoUpdateDto.proveedor());
           } else {
               proveedor = productoActual.getProveedor();
           }
           Producto productMapped = productoMapper.toProducto(productoUpdateDto, productoActual, categoria, proveedor);
           Producto productUpdated = productoRepository.save(productMapped);
           onChange(Notificacion.Tipo.UPDATE, productUpdated);
           return productUpdated;
       } catch (CategoriaNotFoundException e){
           throw new ProductoBadRequest("La categoria con nombre " + productoUpdateDto.categoria() + " no existe");
       } catch (ProveedorNotFoundException e){
           throw new ProductoBadRequest("El proveedor con nif " + productoUpdateDto.proveedor() + " no existe");
       }
    }

    /**
     * Actualiza la imagen de un producto en la base de datos.
     *
     * @param id   Identificador único del producto.
     * @param file Archivo de imagen a cargar.
     * @return El producto con la imagen actualizada.
     * @throws ProductoNotFound Si no se encuentra un producto con el identificador proporcionado.
     */
    @Override
    @CachePut
    public Producto updateImg(String id, MultipartFile file){
        log.info("Actualizando imagen de producto por id: " + id);
        Producto productoActual = this.findById(id);
        String img = storageService.store(file);
        String urlImg = storageService.getUrl(img).replace(" ", "");
        if (!productoActual.getImagen().equals(Producto.IMAGE_DEFAULT)){
            storageService.delete(productoActual.getImagen());
        }
        productoActual.setImagen(urlImg);
        Producto productUpdated = productoRepository.save(productoActual);
        onChange(Notificacion.Tipo.UPDATE, productUpdated);
        return productUpdated;
    }

    /**
     * Elimina un producto por su identificador único.
     *
     * @param id Identificador único del producto a eliminar.
     * @throws ProductoNotFound Si no se encuentra un producto con el identificador proporcionado.
     */
    @Override
    @CacheEvict
    public void deleteById(String id) {
        log.debug("Borrando producto por id: " + id);
        Producto productoActual = this.findById(id);
        productoRepository.deleteById(productoActual.getId());

        if(productoActual.getImagen() != null && !productoActual.getImagen().equals(Producto.IMAGE_DEFAULT)){
            storageService.delete(productoActual.getImagen());
        }
        onChange(Notificacion.Tipo.DELETE, productoActual);
    }


    /**
     * Realiza acciones específicas cuando cambia un producto, como enviar notificaciones WebSocket.
     *
     * @param tipo Tipo de notificación.
     * @param data Datos relacionados con el producto que cambió.
     */
    public void onChange(Notificacion.Tipo tipo, Producto data) {
        log.debug("Servicio de Productos onChange con tipo: " + tipo + " y datos: " + data);

        if (webSocketService == null) {
            log.warn("No se ha podido enviar la notificación a los clientes ws, no se ha encontrado el servicio");
            webSocketService = this.webSocketConfig.webSocketHandler();
        }

        try {
            Notificacion<NotificacionResponseDto> notificacion = new Notificacion<>(
                    "PRODUCTOS",
                    tipo,
                    productoNotificacionMapper.getNotificacionResponseDto(data, "PRODUCTOS"),
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
     * Establece el servicio WebSocket para pruebas unitarias.
     *
     * @param webSocketHandlerMock Implementación mock del servicio WebSocketHandler.
     */
    public void setWebSocketService(WebSocketHandler webSocketHandlerMock) {
        this.webSocketService = webSocketHandlerMock;
    }
}






