package com.example.demo.controller;

import com.example.demo.dto.ProductoDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.IProductoService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IProductoService productoService;

    private final JsonMapper jsonMapper = JsonMapper.builder().build();
    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        productoDTO = ProductoDTO.builder()
                .id(1L)
                .nombre("Leche")
                .categoria("Lácteos")
                .precio(new BigDecimal("75.50"))
                .cantidad(100)
                .build();
    }

    @Nested
    @DisplayName("GET /api/supermercado/productos")
    class ListarProductos {

        @Test
        @DisplayName("debe retornar 200 con página de productos")
        void debeRetornar200() throws Exception {
            when(productoService.obtenerProductos(any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(productoDTO)));

            mockMvc.perform(get("/api/supermercado/productos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].nombre").value("Leche"))
                    .andExpect(jsonPath("$.content[0].precio").value(75.50));
        }
    }

    @Nested
    @DisplayName("GET /api/supermercado/productos/{id}")
    class ObtenerPorId {

        @Test
        @DisplayName("debe retornar 200 con producto")
        void debeRetornar200() throws Exception {
            when(productoService.obtenerProductoPorId(1L)).thenReturn(productoDTO);

            mockMvc.perform(get("/api/supermercado/productos/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Leche"));
        }

        @Test
        @DisplayName("debe retornar 404 si no existe")
        void debeRetornar404() throws Exception {
            when(productoService.obtenerProductoPorId(99L))
                    .thenThrow(new ResourceNotFoundException("Producto no encontrado"));

            mockMvc.perform(get("/api/supermercado/productos/99"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Producto no encontrado"));
        }
    }

    @Nested
    @DisplayName("POST /api/supermercado/productos")
    class CrearProducto {

        @Test
        @DisplayName("debe retornar 201 al crear producto válido")
        void debeRetornar201() throws Exception {
            when(productoService.crearProducto(any(ProductoDTO.class))).thenReturn(productoDTO);

            mockMvc.perform(post("/api/supermercado/productos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(productoDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nombre").value("Leche"));
        }

        @Test
        @DisplayName("debe retornar 400 con datos inválidos")
        void debeRetornar400() throws Exception {
            ProductoDTO invalido = ProductoDTO.builder()
                    .nombre("")
                    .categoria("")
                    .precio(new BigDecimal("-10"))
                    .cantidad(-5)
                    .build();

            mockMvc.perform(post("/api/supermercado/productos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(invalido)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.details").isArray());
        }
    }

    @Nested
    @DisplayName("PUT /api/supermercado/productos/{id}")
    class ActualizarProducto {

        @Test
        @DisplayName("debe retornar 200 al actualizar")
        void debeRetornar200() throws Exception {
            when(productoService.actualizarProducto(eq(1L), any(ProductoDTO.class)))
                    .thenReturn(productoDTO);

            mockMvc.perform(put("/api/supermercado/productos/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonMapper.writeValueAsString(productoDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Leche"));
        }
    }

    @Nested
    @DisplayName("DELETE /api/supermercado/productos/{id}")
    class EliminarProducto {

        @Test
        @DisplayName("debe retornar 204 al eliminar")
        void debeRetornar204() throws Exception {
            mockMvc.perform(delete("/api/supermercado/productos/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("debe retornar 404 si no existe")
        void debeRetornar404() throws Exception {
            doThrow(new ResourceNotFoundException("Producto no encontrado"))
                    .when(productoService).eliminarProducto(99L);

            mockMvc.perform(delete("/api/supermercado/productos/99"))
                    .andExpect(status().isNotFound());
        }
    }
}
