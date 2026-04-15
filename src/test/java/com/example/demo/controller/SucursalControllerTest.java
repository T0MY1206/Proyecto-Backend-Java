package com.example.demo.controller;

import com.example.demo.dto.SucursalDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.ISucursalService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SucursalController.class)
class SucursalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ISucursalService sucursalService;

    private final JsonMapper jsonMapper = JsonMapper.builder().build();
    private SucursalDTO sucursalDTO;

    @BeforeEach
    void setUp() {
        sucursalDTO = SucursalDTO.builder()
                .id(1L)
                .nombre("Sucursal Centro")
                .direccion("Av. Principal 123")
                .build();
    }

    @Nested
    @DisplayName("GET /api/supermercado/sucursales")
    class ListarSucursales {

        @Test
        @DisplayName("debe retornar 200 con página de sucursales")
        void debeRetornar200() throws Exception {
            when(sucursalService.obtenerSucursales(any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(sucursalDTO)));

            mockMvc.perform(get("/api/supermercado/sucursales"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].nombre").value("Sucursal Centro"));
        }
    }

    @Nested
    @DisplayName("GET /api/supermercado/sucursales/{id}")
    class ObtenerPorId {

        @Test
        @DisplayName("debe retornar 200 con sucursal")
        void debeRetornar200() throws Exception {
            when(sucursalService.obtenerSucursalPorId(1L)).thenReturn(sucursalDTO);

            mockMvc.perform(get("/api/supermercado/sucursales/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Sucursal Centro"));
        }

        @Test
        @DisplayName("debe retornar 404 si no existe")
        void debeRetornar404() throws Exception {
            when(sucursalService.obtenerSucursalPorId(99L))
                    .thenThrow(new ResourceNotFoundException("Sucursal no encontrada"));

            mockMvc.perform(get("/api/supermercado/sucursales/99"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /api/supermercado/sucursales")
    class CrearSucursal {

        @Test
        @DisplayName("debe retornar 201 al crear sucursal válida")
        void debeRetornar201() throws Exception {
            when(sucursalService.crearSucursal(any(SucursalDTO.class))).thenReturn(sucursalDTO);

            mockMvc.perform(post("/api/supermercado/sucursales")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(sucursalDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nombre").value("Sucursal Centro"));
        }

        @Test
        @DisplayName("debe retornar 400 con datos inválidos")
        void debeRetornar400() throws Exception {
            SucursalDTO invalida = SucursalDTO.builder()
                    .nombre("")
                    .direccion("")
                    .build();

            mockMvc.perform(post("/api/supermercado/sucursales")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(invalida)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PUT /api/supermercado/sucursales/{id}")
    class ActualizarSucursal {

        @Test
        @DisplayName("debe retornar 200 al actualizar")
        void debeRetornar200() throws Exception {
            when(sucursalService.actualizarSucursal(eq(1L), any(SucursalDTO.class)))
                    .thenReturn(sucursalDTO);

            mockMvc.perform(put("/api/supermercado/sucursales/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(sucursalDTO)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("DELETE /api/supermercado/sucursales/{id}")
    class EliminarSucursal {

        @Test
        @DisplayName("debe retornar 204 al eliminar")
        void debeRetornar204() throws Exception {
            mockMvc.perform(delete("/api/supermercado/sucursales/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("debe retornar 404 si no existe")
        void debeRetornar404() throws Exception {
            doThrow(new ResourceNotFoundException("Sucursal no encontrada"))
                    .when(sucursalService).eliminarSucursal(99L);

            mockMvc.perform(delete("/api/supermercado/sucursales/99"))
                    .andExpect(status().isNotFound());
        }
    }
}
