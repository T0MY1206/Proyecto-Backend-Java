package com.example.demo.mapper;

import com.example.demo.dto.SucursalDTO;
import com.example.demo.model.Sucursal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SucursalMapper {

    SucursalDTO toDTO(Sucursal sucursal);

    Sucursal toEntity(SucursalDTO dto);
}
