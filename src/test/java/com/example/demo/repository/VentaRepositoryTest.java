package com.example.demo.repository;

import com.example.demo.model.DetalleVenta;
import com.example.demo.model.Producto;
import com.example.demo.model.Sucursal;
import com.example.demo.model.Venta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class VentaRepositoryTest {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Sucursal sucursal;
    private Producto producto;

    @BeforeEach
    void setUp() {
        sucursal = entityManager.persistAndFlush(Sucursal.builder()
                .nombre("Sucursal Centro")
                .direccion("Av. Principal 123")
                .build());

        producto = entityManager.persistAndFlush(Producto.builder()
                .nombre("Leche")
                .categoria("Lácteos")
                .precio(new BigDecimal("75.50"))
                .cantidad(100)
                .build());
    }

    @Test
    @DisplayName("debe guardar venta con detalle en cascada")
    void debeGuardarVentaConDetalle() {
        Venta venta = Venta.builder()
                .fecha(LocalDate.of(2025, 2, 20))
                .estado("COMPLETADA")
                .total(new BigDecimal("151.00"))
                .sucursal(sucursal)
                .detalle(List.of())
                .build();

        DetalleVenta detalle = DetalleVenta.builder()
                .cantProd(2)
                .precio(new BigDecimal("75.50"))
                .producto(producto)
                .venta(venta)
                .build();

        venta.setDetalle(List.of(detalle));

        Venta guardada = ventaRepository.saveAndFlush(venta);

        assertThat(guardada.getId()).isNotNull();
        assertThat(guardada.getDetalle()).hasSize(1);
        assertThat(guardada.getSucursal().getNombre()).isEqualTo("Sucursal Centro");
    }

    @Test
    @DisplayName("debe encontrar venta por id con relaciones")
    void debeEncontrarPorIdConRelaciones() {
        Venta venta = Venta.builder()
                .fecha(LocalDate.now())
                .estado("PENDIENTE")
                .total(BigDecimal.ZERO)
                .sucursal(sucursal)
                .detalle(List.of())
                .build();

        Venta guardada = ventaRepository.saveAndFlush(venta);
        entityManager.clear();

        Optional<Venta> encontrada = ventaRepository.findById(guardada.getId());

        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getEstado()).isEqualTo("PENDIENTE");
    }
}
