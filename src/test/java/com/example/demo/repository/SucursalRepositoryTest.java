package com.example.demo.repository;

import com.example.demo.model.Sucursal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SucursalRepositoryTest {

    @Autowired
    private SucursalRepository sucursalRepository;

    @Test
    @DisplayName("debe guardar y recuperar una sucursal")
    void debeGuardarYRecuperarSucursal() {
        Sucursal sucursal = Sucursal.builder()
                .nombre("Sucursal Centro")
                .direccion("Av. Principal 123")
                .build();

        Sucursal guardada = sucursalRepository.save(sucursal);

        assertThat(guardada.getId()).isNotNull();
        assertThat(guardada.getNombre()).isEqualTo("Sucursal Centro");
    }

    @Test
    @DisplayName("debe encontrar sucursal por id")
    void debeEncontrarPorId() {
        Sucursal sucursal = sucursalRepository.save(Sucursal.builder()
                .nombre("Sucursal Norte")
                .direccion("Calle 456")
                .build());

        Optional<Sucursal> encontrada = sucursalRepository.findById(sucursal.getId());

        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getDireccion()).isEqualTo("Calle 456");
    }

    @Test
    @DisplayName("debe eliminar una sucursal")
    void debeEliminarSucursal() {
        Sucursal sucursal = sucursalRepository.save(Sucursal.builder()
                .nombre("Sucursal Sur")
                .direccion("Av. Sur 789")
                .build());

        sucursalRepository.deleteById(sucursal.getId());

        assertThat(sucursalRepository.findById(sucursal.getId())).isEmpty();
    }
}
