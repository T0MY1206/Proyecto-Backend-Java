package com.example.demo.controller;


import com.example.demo.dto.VentaDTO;
import com.example.demo.service.IVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    @Autowired
    private IVentaService ventaService;

    @GetMapping
    public ResponseEntity<List<VentaDTO>> obtenerVentas(){
        return ResponseEntity.ok(ventaService.obtenerVentas());
    }

    @PostMapping
    public ResponseEntity<VentaDTO> crearVenta(@RequestBody VentaDTO ventaDTO){

        VentaDTO venta = ventaService.crearVenta(ventaDTO);
        return ResponseEntity.created(URI.create("/api/ventas/" + venta.getId())).body(venta);
    }

    @PutMapping("/{id}")
    public VentaDTO actualizarVenta(@PathVariable Long id, @RequestBody VentaDTO ventaDTO){
        return ventaService.actualizarVenta(id, ventaDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<VentaDTO> eliminarVenta(@PathVariable Long id, @RequestBody VentaDTO ventaDTO){
        ventaService.eliminarVenta(id);
        return ResponseEntity.noContent().build();
    }
}
