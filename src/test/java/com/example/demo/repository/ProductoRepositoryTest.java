package com.example.demo.repository;

import com.example.demo.model.Producto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    @DisplayName("debe guardar y recuperar un producto")
    void debeGuardarYRecuperarProducto() {
        Producto producto = Producto.builder()
                .nombre("Leche")
                .categoria("Lácteos")
                .precio(new BigDecimal("75.50"))
                .cantidad(100)
                .build();

        Producto guardado = productoRepository.save(producto);

        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getNombre()).isEqualTo("Leche");
    }

    @Test
    @DisplayName("debe encontrar producto por nombre")
    void debeEncontrarPorNombre() {
        productoRepository.save(Producto.builder()
                .nombre("Pan")
                .categoria("Panadería")
                .precio(new BigDecimal("50.00"))
                .cantidad(200)
                .build());

        Optional<Producto> encontrado = productoRepository.findByNombre("Pan");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getCategoria()).isEqualTo("Panadería");
    }

    @Test
    @DisplayName("debe retornar vacío para nombre inexistente")
    void debeRetornarVacioParaNombreInexistente() {
        Optional<Producto> encontrado = productoRepository.findByNombre("Inexistente");

        assertThat(encontrado).isEmpty();
    }

    @Test
    @DisplayName("debe eliminar un producto")
    void debeEliminarProducto() {
        Producto producto = productoRepository.save(Producto.builder()
                .nombre("Agua")
                .categoria("Bebidas")
                .precio(new BigDecimal("25.00"))
                .cantidad(500)
                .build());

        productoRepository.deleteById(producto.getId());

        assertThat(productoRepository.findById(producto.getId())).isEmpty();
    }
}
