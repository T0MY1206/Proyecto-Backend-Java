package com.example.demo.mapper;

import com.example.demo.dto.VentaDTO;
import com.example.demo.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class VentaMapperTest {

    @Autowired
    private VentaMapper ventaMapper;

    @Test
    @DisplayName("debe convertir Venta a VentaDTO con cálculo de total")
    void debeConvertirVentaADTO() {
        Producto producto = Producto.builder()
                .id(1L).nombre("Leche").categoria("Lácteos")
                .precio(new BigDecimal("75.50")).cantidad(100).build();

        Sucursal sucursal = Sucursal.builder()
                .id(1L).nombre("Centro").direccion("Calle 1").build();

        Venta venta = Venta.builder()
                .id(1L)
                .fecha(LocalDate.of(2025, 2, 20))
                .estado("COMPLETADA")
                .total(new BigDecimal("151.00"))
                .sucursal(sucursal)
                .detalle(List.of())
                .build();

        DetalleVenta detalle = DetalleVenta.builder()
                .id(1L)
                .cantProd(2)
                .precio(new BigDecimal("75.50"))
                .producto(producto)
                .venta(venta)
                .build();

        venta.setDetalle(List.of(detalle));

        VentaDTO dto = ventaMapper.toDTO(venta);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getIdSucursal()).isEqualTo(1L);
        assertThat(dto.getEstado()).isEqualTo("COMPLETADA");
        assertThat(dto.getTotal()).isEqualByComparingTo(new BigDecimal("151.00"));
        assertThat(dto.getDetalle()).hasSize(1);
        assertThat(dto.getDetalle().getFirst().getId()).isEqualTo(1L);
        assertThat(dto.getDetalle().getFirst().getNombreProd()).isEqualTo("Leche");
        assertThat(dto.getDetalle().getFirst().getSubtotal())
                .isEqualByComparingTo(new BigDecimal("151.00"));
    }

    @Test
    @DisplayName("debe retornar null cuando la venta es null")
    void debeRetornarNullConNull() {
        assertThat(ventaMapper.toDTO(null)).isNull();
    }
}
