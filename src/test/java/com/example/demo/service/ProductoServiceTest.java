package com.example.demo.service;

import com.example.demo.dto.ProductoDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.ProductoMapper;
import com.example.demo.model.Producto;
import com.example.demo.repository.ProductoRepository;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;
    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id(1L)
                .nombre("Leche")
                .categoria("Lácteos")
                .precio(new BigDecimal("75.50"))
                .cantidad(100)
                .build();

        productoDTO = ProductoDTO.builder()
                .id(1L)
                .nombre("Leche")
                .categoria("Lácteos")
                .precio(new BigDecimal("75.50"))
                .cantidad(100)
                .build();
    }

    @Nested
    @DisplayName("obtenerProductos")
    class ObtenerProductos {

        @Test
        @DisplayName("debe retornar página de productos")
        void debeRetornarPaginaDeProductos() {
            Pageable pageable = PageRequest.of(0, 20);
            Page<Producto> page = new PageImpl<>(List.of(producto), pageable, 1);

            when(productoRepository.findAll(pageable)).thenReturn(page);
            when(productoMapper.toDTO(producto)).thenReturn(productoDTO);

            Page<ProductoDTO> resultado = productoService.obtenerProductos(pageable);

            assertThat(resultado.getContent()).hasSize(1);
            assertThat(resultado.getContent().getFirst().getNombre()).isEqualTo("Leche");
            verify(productoRepository).findAll(pageable);
        }

        @Test
        @DisplayName("debe retornar página vacía cuando no hay productos")
        void debeRetornarPaginaVacia() {
            Pageable pageable = PageRequest.of(0, 20);
            Page<Producto> page = new PageImpl<>(List.of(), pageable, 0);

            when(productoRepository.findAll(pageable)).thenReturn(page);

            Page<ProductoDTO> resultado = productoService.obtenerProductos(pageable);

            assertThat(resultado.getContent()).isEmpty();
        }
    }

    @Nested
    @DisplayName("obtenerProductoPorId")
    class ObtenerProductoPorId {

        @Test
        @DisplayName("debe retornar producto cuando existe")
        void debeRetornarProducto() {
            when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
            when(productoMapper.toDTO(producto)).thenReturn(productoDTO);

            ProductoDTO resultado = productoService.obtenerProductoPorId(1L);

            assertThat(resultado.getNombre()).isEqualTo("Leche");
        }

        @Test
        @DisplayName("debe lanzar excepción cuando no existe")
        void debeLanzarExcepcion() {
            when(productoRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productoService.obtenerProductoPorId(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Producto no encontrado");
        }
    }

    @Nested
    @DisplayName("crearProducto")
    class CrearProducto {

        @Test
        @DisplayName("debe crear y retornar producto")
        void debeCrearProducto() {
            when(productoMapper.toEntity(productoDTO)).thenReturn(producto);
            when(productoRepository.save(any(Producto.class))).thenReturn(producto);
            when(productoMapper.toDTO(producto)).thenReturn(productoDTO);

            ProductoDTO resultado = productoService.crearProducto(productoDTO);

            assertThat(resultado.getNombre()).isEqualTo("Leche");
            assertThat(resultado.getPrecio()).isEqualByComparingTo(new BigDecimal("75.50"));
            verify(productoRepository).save(any(Producto.class));
        }
    }

    @Nested
    @DisplayName("actualizarProducto")
    class ActualizarProducto {

        @Test
        @DisplayName("debe actualizar producto existente")
        void debeActualizarProducto() {
            ProductoDTO actualizado = ProductoDTO.builder()
                    .nombre("Leche Descremada")
                    .categoria("Lácteos")
                    .precio(new BigDecimal("85.00"))
                    .cantidad(50)
                    .build();

            when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
            when(productoRepository.save(any(Producto.class))).thenReturn(producto);
            when(productoMapper.toDTO(any(Producto.class))).thenReturn(actualizado);

            ProductoDTO resultado = productoService.actualizarProducto(1L, actualizado);

            assertThat(resultado.getNombre()).isEqualTo("Leche Descremada");
            verify(productoRepository).save(any(Producto.class));
        }

        @Test
        @DisplayName("debe lanzar excepción si el producto no existe")
        void debeLanzarExcepcionSiNoExiste() {
            when(productoRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productoService.actualizarProducto(99L, productoDTO))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("eliminarProducto")
    class EliminarProducto {

        @Test
        @DisplayName("debe eliminar producto existente")
        void debeEliminarProducto() {
            when(productoRepository.existsById(1L)).thenReturn(true);

            productoService.eliminarProducto(1L);

            verify(productoRepository).deleteById(1L);
        }

        @Test
        @DisplayName("debe lanzar excepción si el producto no existe")
        void debeLanzarExcepcionSiNoExiste() {
            when(productoRepository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> productoService.eliminarProducto(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
