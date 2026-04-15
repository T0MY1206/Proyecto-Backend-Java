package com.example.demo.service;

import com.example.demo.dto.DetalleVentaDTO;
import com.example.demo.dto.VentaDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.VentaMapper;
import com.example.demo.model.*;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.SucursalRepository;
import com.example.demo.repository.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private SucursalRepository sucursalRepository;
    @Mock
    private VentaMapper ventaMapper;

    @InjectMocks
    private VentaService ventaService;

    private Sucursal sucursal;
    private Producto producto;
    private Venta venta;
    private VentaDTO ventaDTO;

    @BeforeEach
    void setUp() {
        sucursal = Sucursal.builder().id(1L).nombre("Centro").direccion("Calle 1").build();
        producto = Producto.builder().id(1L).nombre("Leche").categoria("Lácteos")
                .precio(new BigDecimal("75.50")).cantidad(100).build();

        venta = Venta.builder()
                .id(1L)
                .fecha(LocalDate.of(2025, 2, 20))
                .estado("COMPLETADA")
                .total(new BigDecimal("151.00"))
                .sucursal(sucursal)
                .build();

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
    @DisplayName("obtenerVentas")
    class ObtenerVentas {

        @Test
        @DisplayName("debe retornar página de ventas")
        void debeRetornarPaginaDeVentas() {
            Pageable pageable = PageRequest.of(0, 20);
            Page<Venta> page = new PageImpl<>(List.of(venta), pageable, 1);

            when(ventaRepository.findAll(pageable)).thenReturn(page);
            when(ventaMapper.toDTO(venta)).thenReturn(ventaDTO);

            Page<VentaDTO> resultado = ventaService.obtenerVentas(pageable);

            assertThat(resultado.getContent()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("obtenerVentaPorId")
    class ObtenerVentaPorId {

        @Test
        @DisplayName("debe retornar venta cuando existe")
        void debeRetornarVenta() {
            when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
            when(ventaMapper.toDTO(venta)).thenReturn(ventaDTO);

            VentaDTO resultado = ventaService.obtenerVentaPorId(1L);

            assertThat(resultado.getEstado()).isEqualTo("COMPLETADA");
        }

        @Test
        @DisplayName("debe lanzar excepción cuando no existe")
        void debeLanzarExcepcion() {
            when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> ventaService.obtenerVentaPorId(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("crearVenta")
    class CrearVenta {

        @Test
        @DisplayName("debe crear venta con detalle y calcular total")
        void debeCrearVentaConDetalle() {
            when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
            when(productoRepository.findByNombre("Leche")).thenReturn(Optional.of(producto));
            when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
            when(ventaMapper.toDTO(any(Venta.class))).thenReturn(ventaDTO);

            VentaDTO resultado = ventaService.crearVenta(ventaDTO);

            assertThat(resultado.getTotal()).isEqualByComparingTo(new BigDecimal("151.00"));
            verify(ventaRepository).save(any(Venta.class));
        }

        @Test
        @DisplayName("debe lanzar excepción si la sucursal no existe")
        void debeLanzarExcepcionSucursalNoExiste() {
            when(sucursalRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> ventaService.crearVenta(ventaDTO))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Sucursal");
        }

        @Test
        @DisplayName("debe lanzar excepción si un producto no existe")
        void debeLanzarExcepcionProductoNoExiste() {
            when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
            when(productoRepository.findByNombre("Leche")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> ventaService.crearVenta(ventaDTO))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Producto no encontrado");
        }
    }

    @Nested
    @DisplayName("actualizarVenta")
    class ActualizarVenta {

        @Test
        @DisplayName("debe actualizar venta existente")
        void debeActualizarVenta() {
            VentaDTO actualizacion = VentaDTO.builder()
                    .fecha(LocalDate.of(2025, 3, 1))
                    .estado("ANULADA")
                    .build();

            when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
            when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
            when(ventaMapper.toDTO(any(Venta.class))).thenReturn(ventaDTO);

            VentaDTO resultado = ventaService.actualizarVenta(1L, actualizacion);

            assertThat(resultado).isNotNull();
            verify(ventaRepository).save(any(Venta.class));
        }

        @Test
        @DisplayName("debe lanzar excepción si no existe")
        void debeLanzarExcepcion() {
            when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> ventaService.actualizarVenta(99L, ventaDTO))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("eliminarVenta")
    class EliminarVenta {

        @Test
        @DisplayName("debe eliminar venta existente")
        void debeEliminarVenta() {
            when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

            ventaService.eliminarVenta(1L);

            verify(ventaRepository).delete(venta);
        }

        @Test
        @DisplayName("debe lanzar excepción si no existe")
        void debeLanzarExcepcion() {
            when(ventaRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> ventaService.eliminarVenta(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
