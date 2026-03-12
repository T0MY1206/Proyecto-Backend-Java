package com.example.demo.controller;

import com.example.demo.dto.ProductoDTO;
import com.example.demo.service.IProductoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Supermercado - Productos", description = "API de productos del dominio supermercado")
@RestController
@RequestMapping("/api/supermercado/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> obtenerProductos(){
        return ResponseEntity.ok(productoService.obtenerProductos());
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crearProducto(@RequestBody ProductoDTO productoDTO){

        ProductoDTO producto = productoService.crearProducto(productoDTO);

        return ResponseEntity.created(URI.create("/api/supermercado/productos/" + producto.getId())).body(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO){
        return ResponseEntity.ok(productoService.actualizarProducto(id, productoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id){
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
