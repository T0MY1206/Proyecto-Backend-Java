package com.example.demo.controller;

import com.example.demo.dto.DetalleVentaDTO;
import com.example.demo.dto.VentaDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.IVentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class)
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IVentaService ventaService;

    private final JsonMapper jsonMapper = JsonMapper.builder().build();
    private VentaDTO ventaDTO;

    @BeforeEach
    void setUp() {
        ventaDTO = VentaDTO.builder()
                .id(1L)
                .fecha(LocalDate.of(2025, 2, 20))
                .estado("COMPLETADA")
                .idSucursal(1L)
                .total(new BigDecimal("151.00"))
                .detalle(List.of(
                        DetalleVentaDTO.builder()
                                .nombreProd("Leche")
                                .cantProd(2)
                                .precio(new BigDecimal("75.50"))
                                .subtotal(new BigDecimal("151.00"))
                                .build()
                ))
                .build();
    }

    @Nested
    @DisplayName("GET /api/supermercado/ventas")
    class ListarVentas {

        @Test
        @DisplayName("debe retornar 200 con página de ventas")
        void debeRetornar200() throws Exception {
            when(ventaService.obtenerVentas(any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(ventaDTO)));

            mockMvc.perform(get("/api/supermercado/ventas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].estado").value("COMPLETADA"));
        }
    }

    @Nested
    @DisplayName("GET /api/supermercado/ventas/{id}")
    class ObtenerPorId {

        @Test
        @DisplayName("debe retornar 200 con venta")
        void debeRetornar200() throws Exception {
            when(ventaService.obtenerVentaPorId(1L)).thenReturn(ventaDTO);

            mockMvc.perform(get("/api/supermercado/ventas/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estado").value("COMPLETADA"))
                    .andExpect(jsonPath("$.total").value(151.00));
        }

        @Test
        @DisplayName("debe retornar 404 si no existe")
        void debeRetornar404() throws Exception {
            when(ventaService.obtenerVentaPorId(99L))
                    .thenThrow(new ResourceNotFoundException("Venta no encontrada"));

            mockMvc.perform(get("/api/supermercado/ventas/99"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /api/supermercado/ventas")
    class CrearVenta {

        @Test
        @DisplayName("debe retornar 201 al crear venta válida")
        void debeRetornar201() throws Exception {
            when(ventaService.crearVenta(any(VentaDTO.class))).thenReturn(ventaDTO);

            mockMvc.perform(post("/api/supermercado/ventas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(ventaDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.estado").value("COMPLETADA"));
        }

        @Test
        @DisplayName("debe retornar 400 con detalle vacío")
        void debeRetornar400SinDetalle() throws Exception {
            VentaDTO invalida = VentaDTO.builder()
                    .fecha(LocalDate.now())
                    .estado("COMPLETADA")
                    .idSucursal(1L)
                    .detalle(List.of())
                    .build();

            mockMvc.perform(post("/api/supermercado/ventas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(invalida)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("debe retornar 400 sin fecha")
        void debeRetornar400SinFecha() throws Exception {
            VentaDTO invalida = VentaDTO.builder()
                    .estado("COMPLETADA")
                    .idSucursal(1L)
                    .detalle(List.of(
                            DetalleVentaDTO.builder()
                                    .nombreProd("Leche")
                                    .cantProd(1)
                                    .precio(new BigDecimal("75.50"))
                                    .build()
                    ))
                    .build();

            mockMvc.perform(post("/api/supermercado/ventas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(invalida)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("DELETE /api/supermercado/ventas/{id}")
    class EliminarVenta {

        @Test
        @DisplayName("debe retornar 204 al eliminar")
        void debeRetornar204() throws Exception {
            mockMvc.perform(delete("/api/supermercado/ventas/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
