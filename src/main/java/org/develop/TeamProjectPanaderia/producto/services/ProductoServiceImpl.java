package org.develop.TeamProjectPanaderia.producto.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Join;
import lombok.extern.slf4j.Slf4j;

import org.develop.TeamProjectPanaderia.WebSockets.dto.NotificacionResponseDto;
import org.develop.TeamProjectPanaderia.WebSockets.mapper.NotificacionMapper;
import org.develop.TeamProjectPanaderia.WebSockets.model.Notificacion;
import org.develop.TeamProjectPanaderia.categoria.models.Categoria;
import org.develop.TeamProjectPanaderia.categoria.services.CategoriaService;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketConfig;
import org.develop.TeamProjectPanaderia.config.websockets.WebSocketHandler;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoCreateDto;
import org.develop.TeamProjectPanaderia.producto.dto.ProductoUpdateDto;
import org.develop.TeamProjectPanaderia.producto.exceptions.ProductoBadUuid;
import org.develop.TeamProjectPanaderia.producto.exceptions.ProductoNotFound;
import org.develop.TeamProjectPanaderia.producto.exceptions.ProductoNotSaved;
import org.develop.TeamProjectPanaderia.producto.mapper.ProductoMapper;
import org.develop.TeamProjectPanaderia.producto.models.Producto;
import org.develop.TeamProjectPanaderia.producto.repositories.ProductoRepository;
import org.develop.TeamProjectPanaderia.proveedores.models.Proveedor;
import org.develop.TeamProjectPanaderia.proveedores.services.ProveedorService;
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
        webSocketService = webSocketConfig.webSocketHandler();
        this.mapper = mapper;
        this.productoNotificacionMapper = productoNotificacionMapper;
    }

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
                categoria.map(c ->{
                    Join<Producto, Proveedor> proveedorJoin = root.join("proveedor");
                    return criteriaBuilder.like(criteriaBuilder.lower(proveedorJoin.get("NIF")), "%" + c.toLowerCase() + "%");
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Producto> criterio = Specification.where(specNombreProducto)
                .and(specStockMinProducto)
                .and(specPrecioMaxProducto)
                .and(specIsActivo)
                .and(specCategoriaProducto)
                .and(specProveedorProducto);
        return productoRepository.findAll(criterio, pageable);
    }

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

    @Override
    @Cacheable
    public Producto findByName(String name) {
        log.info("Buscando producto por nombre: " + name);
        return productoRepository.findByNombreEqualsIgnoreCase(name).orElseThrow(() -> new ProductoNotFound(name));
    }

    @Override
    @CachePut
    public Producto save(ProductoCreateDto productoCreateDto) {
        log.info("Guardando producto: " + productoCreateDto);
        if(productoRepository.findByNombreEqualsIgnoreCase(productoCreateDto.nombre()).isPresent()) {
            throw new ProductoNotSaved(productoCreateDto.nombre());
        }
        Categoria categoria = categoriaService.findByName(productoCreateDto.categoria());
        Proveedor proveedores = proveedoresService.findProveedoresByNIF(productoCreateDto.proveedor());
        UUID id = UUID.randomUUID();
        Producto productoMapped = productoMapper.toProducto(id,productoCreateDto, categoria, proveedores);
        Producto productoSaved = productoRepository.save(productoMapped);
        onChange(Notificacion.Tipo.CREATE, productoSaved);
        return productoSaved;
    }

    @Override
    @CachePut
    public Producto update(String id, ProductoUpdateDto productoUpdateDto) {
       log.info("Actualizando producto por id: " + id);
       Producto productoActual = this.findById(id);
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
    }

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

    void onChange(Notificacion.Tipo tipo, Producto data) {
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

    public void setWebSocketService(WebSocketHandler webSocketHandlerMock) {
        this.webSocketService = webSocketHandlerMock;
    }
}






