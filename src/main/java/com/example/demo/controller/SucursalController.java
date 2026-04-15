package com.example.demo.controller;

import com.example.demo.dto.SucursalDTO;
import com.example.demo.service.ISucursalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Supermercado - Sucursales", description = "API de sucursales del dominio supermercado")
@RestController
@RequestMapping("/api/supermercado/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final ISucursalService sucursalService;

    @GetMapping
    public ResponseEntity<Page<SucursalDTO>> obtenerSucursales(
            @PageableDefault(size = 20, sort = "nombre") Pageable pageable) {
        return ResponseEntity.ok(sucursalService.obtenerSucursales(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalDTO> obtenerSucursalPorId(@PathVariable Long id) {
        return ResponseEntity.ok(sucursalService.obtenerSucursalPorId(id));
    }

    @PostMapping
    public ResponseEntity<SucursalDTO> crearSucursal(@Valid @RequestBody SucursalDTO sucursalDTO) {
        SucursalDTO sucursal = sucursalService.crearSucursal(sucursalDTO);
        return ResponseEntity
                .created(URI.create("/api/supermercado/sucursales/" + sucursal.getId()))
                .body(sucursal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalDTO> actualizarSucursal(@PathVariable Long id,
                                                          @Valid @RequestBody SucursalDTO sucursalDTO) {
        return ResponseEntity.ok(sucursalService.actualizarSucursal(id, sucursalDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSucursal(@PathVariable Long id) {
        sucursalService.eliminarSucursal(id);
        return ResponseEntity.noContent().build();
    }
}
