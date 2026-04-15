package com.example.demo.mapper;

import com.example.demo.dto.ProductoDTO;
import com.example.demo.model.Producto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class ProductoMapperTest {

    @Autowired
    private ProductoMapper productoMapper;

    @Test
    @DisplayName("debe convertir Producto a ProductoDTO correctamente")
    void debeConvertirADTO() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Leche")
                .categoria("Lácteos")
                .precio(new BigDecimal("75.50"))
                .cantidad(100)
                .build();

        ProductoDTO dto = productoMapper.toDTO(producto);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNombre()).isEqualTo("Leche");
        assertThat(dto.getCategoria()).isEqualTo("Lácteos");
        assertThat(dto.getPrecio()).isEqualByComparingTo(new BigDecimal("75.50"));
        assertThat(dto.getCantidad()).isEqualTo(100);
    }

    @Test
    @DisplayName("debe convertir ProductoDTO a Producto correctamente")
    void debeConvertirAEntidad() {
        ProductoDTO dto = ProductoDTO.builder()
                .nombre("Pan")
                .categoria("Panadería")
                .precio(new BigDecimal("50.00"))
                .cantidad(200)
                .build();

        Producto producto = productoMapper.toEntity(dto);

        assertThat(producto.getNombre()).isEqualTo("Pan");
        assertThat(producto.getCategoria()).isEqualTo("Panadería");
        assertThat(producto.getPrecio()).isEqualByComparingTo(new BigDecimal("50.00"));
    }

    @Test
    @DisplayName("debe retornar null cuando el producto es null")
    void debeRetornarNullConNull() {
        assertThat(productoMapper.toDTO(null)).isNull();
        assertThat(productoMapper.toEntity(null)).isNull();
    }
}
