package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.dto.ProductoDTO;
import com.example.demo.mapper.ProductoMapper;
import com.example.demo.model.Producto;
import com.example.demo.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductoDTO> obtenerProductos(Pageable pageable) {
        return productoRepository.findAll(pageable)
                .map(productoMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDTO obtenerProductoPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        return productoMapper.toDTO(producto);
    }

    @Override
    @Transactional
    public ProductoDTO crearProducto(ProductoDTO productoDto) {
        log.info("Creando producto: {}", productoDto.getNombre());
        Producto prod = productoMapper.toEntity(productoDto);
        return productoMapper.toDTO(productoRepository.save(prod));
    }

    @Override
    @Transactional
    public ProductoDTO actualizarProducto(Long id, ProductoDTO producto) {
        Producto prod = productoRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        prod.setNombre(producto.getNombre());
        prod.setCategoria(producto.getCategoria());
        prod.setPrecio(producto.getPrecio());
        prod.setCantidad(producto.getCantidad());

        return productoMapper.toDTO(productoRepository.save(prod));
    }

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
        log.info("Producto eliminado: id={}", id);
    }
}
