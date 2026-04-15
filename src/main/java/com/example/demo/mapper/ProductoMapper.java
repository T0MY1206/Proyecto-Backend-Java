package com.example.demo.mapper;

import com.example.demo.dto.ProductoDTO;
import com.example.demo.model.Producto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    ProductoDTO toDTO(Producto producto);

    Producto toEntity(ProductoDTO dto);
}
