package com.example.demo.service;

import com.example.demo.dto.ProductoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductoService {

    Page<ProductoDTO> obtenerProductos(Pageable pageable);

    ProductoDTO obtenerProductoPorId(Long id);

    ProductoDTO crearProducto(ProductoDTO producto);

    ProductoDTO actualizarProducto(Long id, ProductoDTO producto);

    void eliminarProducto(Long id);
}
