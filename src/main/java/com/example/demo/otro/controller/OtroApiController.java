package com.example.demo.otro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador placeholder para futuras APIs de otros proyectos o mejoras.
 * Todas las APIs que no sean del dominio supermercado pueden exponerse bajo /api/otro/...
 * En Swagger aparecerán en el grupo "Otros / Futuras APIs".
 */
@Tag(name = "Otros", description = "APIs de otros proyectos o mejoras futuras (agregar aquí nuevos recursos)")
@RestController
@RequestMapping("/api/otro")
public class OtroApiController {

    @Operation(summary = "Info del contexto Otros", description = "Endpoint de ejemplo para el grupo de APIs futuras")
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> info() {
        return ResponseEntity.ok(Map.of(
                "contexto", "otros",
                "descripcion", "Espacio para futuras APIs o integraciones de otros proyectos",
                "basePath", "/api/otro"
        ));
    }
}
