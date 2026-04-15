package com.example.demo.service;

import com.example.demo.dto.SucursalDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.SucursalMapper;
import com.example.demo.model.Sucursal;
import com.example.demo.repository.SucursalRepository;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private SucursalMapper sucursalMapper;

    @InjectMocks
    private SucursalService sucursalService;

    private Sucursal sucursal;
    private SucursalDTO sucursalDTO;

    @BeforeEach
    void setUp() {
        sucursal = Sucursal.builder()
                .id(1L)
                .nombre("Sucursal Centro")
                .direccion("Av. Principal 123")
                .build();

        sucursalDTO = SucursalDTO.builder()
                .id(1L)
                .nombre("Sucursal Centro")
                .direccion("Av. Principal 123")
                .build();
    }

    @Nested
    @DisplayName("obtenerSucursales")
    class ObtenerSucursales {

        @Test
        @DisplayName("debe retornar página de sucursales")
        void debeRetornarPaginaDeSucursales() {
            Pageable pageable = PageRequest.of(0, 20);
            Page<Sucursal> page = new PageImpl<>(List.of(sucursal), pageable, 1);

            when(sucursalRepository.findAll(pageable)).thenReturn(page);
            when(sucursalMapper.toDTO(sucursal)).thenReturn(sucursalDTO);

            Page<SucursalDTO> resultado = sucursalService.obtenerSucursales(pageable);

            assertThat(resultado.getContent()).hasSize(1);
            assertThat(resultado.getContent().getFirst().getNombre()).isEqualTo("Sucursal Centro");
        }
    }

    @Nested
    @DisplayName("obtenerSucursalPorId")
    class ObtenerSucursalPorId {

        @Test
        @DisplayName("debe retornar sucursal cuando existe")
        void debeRetornarSucursal() {
            when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
            when(sucursalMapper.toDTO(sucursal)).thenReturn(sucursalDTO);

            SucursalDTO resultado = sucursalService.obtenerSucursalPorId(1L);

            assertThat(resultado.getNombre()).isEqualTo("Sucursal Centro");
        }

        @Test
        @DisplayName("debe lanzar excepción cuando no existe")
        void debeLanzarExcepcion() {
            when(sucursalRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sucursalService.obtenerSucursalPorId(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("crearSucursal")
    class CrearSucursal {

        @Test
        @DisplayName("debe crear y retornar sucursal")
        void debeCrearSucursal() {
            when(sucursalMapper.toEntity(sucursalDTO)).thenReturn(sucursal);
            when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursal);
            when(sucursalMapper.toDTO(sucursal)).thenReturn(sucursalDTO);

            SucursalDTO resultado = sucursalService.crearSucursal(sucursalDTO);

            assertThat(resultado.getNombre()).isEqualTo("Sucursal Centro");
            verify(sucursalRepository).save(any(Sucursal.class));
        }
    }

    @Nested
    @DisplayName("actualizarSucursal")
    class ActualizarSucursal {

        @Test
        @DisplayName("debe actualizar sucursal existente")
        void debeActualizarSucursal() {
            SucursalDTO actualizada = SucursalDTO.builder()
                    .nombre("Sucursal Norte")
                    .direccion("Calle 456")
                    .build();

            when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursal));
            when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursal);
            when(sucursalMapper.toDTO(any(Sucursal.class))).thenReturn(actualizada);

            SucursalDTO resultado = sucursalService.actualizarSucursal(1L, actualizada);

            assertThat(resultado.getNombre()).isEqualTo("Sucursal Norte");
        }

        @Test
        @DisplayName("debe lanzar excepción si no existe")
        void debeLanzarExcepcion() {
            when(sucursalRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> sucursalService.actualizarSucursal(99L, sucursalDTO))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("eliminarSucursal")
    class EliminarSucursal {

        @Test
        @DisplayName("debe eliminar sucursal existente")
        void debeEliminarSucursal() {
            when(sucursalRepository.existsById(1L)).thenReturn(true);

            sucursalService.eliminarSucursal(1L);

            verify(sucursalRepository).deleteById(1L);
        }

        @Test
        @DisplayName("debe lanzar excepción si no existe")
        void debeLanzarExcepcion() {
            when(sucursalRepository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> sucursalService.eliminarSucursal(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
