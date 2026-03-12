package com.example.demo.service;

import com.example.demo.Exception.NotFoundException;
import com.example.demo.dto.ProductoDTO;
import com.example.demo.mapper.Mapper;
import com.example.demo.model.Producto;
import com.example.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService implements IProductoService{

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<ProductoDTO> obtenerProductos() {
        return productoRepository.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public ProductoDTO crearProducto(ProductoDTO productoDto) {

        Producto prod = Producto.builder()
                .nombre(productoDto.getNombre())
                .categoria(productoDto.getCategoria())
                .precio(productoDto.getPrecio())
                .cantidad(productoDto.getCantidad())
                .build();
        return Mapper.toDTO(productoRepository.save(prod));
    }

    @Override
    public ProductoDTO actualizarProducto(Long id, ProductoDTO producto) {

        Producto prod = productoRepository.
                findById(id).orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        prod.setNombre(producto.getNombre());
        prod.setCategoria(producto.getCategoria());
        prod.setPrecio(producto.getPrecio());
        prod.setCantidad(producto.getCantidad());

        return Mapper.toDTO(productoRepository.save(prod));
    }

    @Override
    public void eliminarProducto(Long id) {
        if(!productoRepository.existsById(id)){
            throw new NotFoundException("Producto no encontrado");
        }

        productoRepository.deleteById(id);
    }
}
