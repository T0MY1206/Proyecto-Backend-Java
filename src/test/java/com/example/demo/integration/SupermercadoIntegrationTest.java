package com.example.demo.integration;

import com.example.demo.dto.DetalleVentaDTO;
import com.example.demo.dto.ProductoDTO;
import com.example.demo.dto.SucursalDTO;
import com.example.demo.dto.VentaDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SupermercadoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Test
    @Order(1)
    @DisplayName("Flujo completo: crear producto -> crear sucursal -> crear venta -> consultar")
    void flujoCompleto() throws Exception {
        ProductoDTO productoDTO = ProductoDTO.builder()
                .nombre("Leche")
                .categoria("Lácteos")
                .precio(new BigDecimal("75.50"))
                .cantidad(100)
                .build();

        mockMvc.perform(post("/api/supermercado/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Leche"));

        SucursalDTO sucursalDTO = SucursalDTO.builder()
                .nombre("Sucursal Centro")
                .direccion("Av. Principal 123")
                .build();

        MvcResult sucursalResult = mockMvc.perform(post("/api/supermercado/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(sucursalDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Sucursal Centro"))
                .andReturn();

        SucursalDTO sucursalCreada = jsonMapper.readValue(
                sucursalResult.getResponse().getContentAsString(), SucursalDTO.class);

        VentaDTO ventaDTO = VentaDTO.builder()
                .fecha(LocalDate.of(2025, 2, 20))
                .estado("COMPLETADA")
                .idSucursal(sucursalCreada.getId())
                .detalle(List.of(
                        DetalleVentaDTO.builder()
                                .nombreProd("Leche")
                                .cantProd(2)
                                .precio(new BigDecimal("75.50"))
                                .build()
                ))
                .build();

        mockMvc.perform(post("/api/supermercado/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(ventaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("COMPLETADA"))
                .andExpect(jsonPath("$.total").value(151.00))
                .andExpect(jsonPath("$.detalle[0].nombreProd").value("Leche"));

        mockMvc.perform(get("/api/supermercado/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        mockMvc.perform(get("/api/supermercado/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @Order(2)
    @DisplayName("debe retornar 400 al crear producto con datos inválidos")
    void debeRetornar400ProductoInvalido() throws Exception {
        ProductoDTO invalido = ProductoDTO.builder()
                .nombre("")
                .categoria("")
                .precio(new BigDecimal("-1"))
                .cantidad(-5)
                .build();

        mockMvc.perform(post("/api/supermercado/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    @Order(3)
    @DisplayName("debe retornar 404 al buscar producto inexistente")
    void debeRetornar404ProductoInexistente() throws Exception {
        mockMvc.perform(get("/api/supermercado/productos/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Producto no encontrado"));
    }
}
