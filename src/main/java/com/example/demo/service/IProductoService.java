package com.example.demo.service;

import com.example.demo.dto.ProductoDTO;

import java.util.List;

public interface IProductoService {
    List<ProductoDTO> obtenerProductos();
    ProductoDTO crearProducto(ProductoDTO producto);
    ProductoDTO actualizarProducto(Long id, ProductoDTO producto);
    void eliminarProducto(Long id);
}
