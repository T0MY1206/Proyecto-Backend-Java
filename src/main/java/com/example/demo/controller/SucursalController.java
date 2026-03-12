package com.example.demo.controller;

import com.example.demo.dto.SucursalDTO;
import com.example.demo.service.ISucursalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Supermercado - Sucursales", description = "API de sucursales del dominio supermercado")
@RestController
@RequestMapping("/api/supermercado/sucursales")
public class SucursalController {

    @Autowired
    private ISucursalService sucursalService;

    @GetMapping
    public ResponseEntity<List<SucursalDTO>> obtenerSucursales(){
        return ResponseEntity.ok(sucursalService.obtenerSucursales());
    }

    @PostMapping
    public ResponseEntity<SucursalDTO> crearSucursal(@RequestBody SucursalDTO sucursalDTO){
        SucursalDTO sucursal = sucursalService.crearSucursal(sucursalDTO);
        return ResponseEntity.created(URI.create("/api/supermercado/sucursales/" + sucursal.getId())).body(sucursal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalDTO> actualizarSucursal(@PathVariable Long id, @RequestBody SucursalDTO sucursalDTO){
        return ResponseEntity.ok(sucursalService.actualizarSucursal(id, sucursalDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SucursalDTO> eliminarSucursal(@PathVariable Long id){
        sucursalService.eliminarSucursal(id);
        return ResponseEntity.noContent().build();
    }
}
