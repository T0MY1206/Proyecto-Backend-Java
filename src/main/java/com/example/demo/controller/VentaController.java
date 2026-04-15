package com.example.demo.controller;

import com.example.demo.dto.VentaDTO;
import com.example.demo.service.IVentaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Supermercado - Ventas", description = "API de ventas del dominio supermercado")
@RestController
@RequestMapping("/api/supermercado/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final IVentaService ventaService;

    @GetMapping
    public ResponseEntity<Page<VentaDTO>> obtenerVentas(
            @PageableDefault(size = 20, sort = "fecha") Pageable pageable) {
        return ResponseEntity.ok(ventaService.obtenerVentas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> obtenerVentaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.obtenerVentaPorId(id));
    }

    @PostMapping
    public ResponseEntity<VentaDTO> crearVenta(@Valid @RequestBody VentaDTO ventaDTO) {
        VentaDTO venta = ventaService.crearVenta(ventaDTO);
        return ResponseEntity
                .created(URI.create("/api/supermercado/ventas/" + venta.getId()))
                .body(venta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> actualizarVenta(@PathVariable Long id,
                                                    @Valid @RequestBody VentaDTO ventaDTO) {
        return ResponseEntity.ok(ventaService.actualizarVenta(id, ventaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
        return ResponseEntity.noContent().build();
    }
}
