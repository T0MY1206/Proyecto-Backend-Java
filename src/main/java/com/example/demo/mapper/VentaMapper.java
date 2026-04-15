package com.example.demo.mapper;

import com.example.demo.dto.DetalleVentaDTO;
import com.example.demo.dto.VentaDTO;
import com.example.demo.model.DetalleVenta;
import com.example.demo.model.Venta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VentaMapper {

    @Mapping(source = "sucursal.id", target = "idSucursal")
    @Mapping(source = "detalle", target = "detalle", qualifiedByName = "detalleListToDTO")
    @Mapping(source = "detalle", target = "total", qualifiedByName = "calcularTotal")
    VentaDTO toDTO(Venta venta);

    @Named("detalleListToDTO")
    default List<DetalleVentaDTO> detalleListToDTO(List<DetalleVenta> detalles) {
        if (detalles == null) return List.of();
        return detalles.stream().map(this::detalleToDTO).toList();
    }

    default DetalleVentaDTO detalleToDTO(DetalleVenta detalle) {
        return DetalleVentaDTO.builder()
                .id(detalle.getId())
                .nombreProd(detalle.getProducto().getNombre())
                .cantProd(detalle.getCantProd())
                .precio(detalle.getPrecio())
                .subtotal(detalle.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantProd())))
                .build();
    }

    @Named("calcularTotal")
    default BigDecimal calcularTotal(List<DetalleVenta> detalles) {
        if (detalles == null) return BigDecimal.ZERO;
        return detalles.stream()
                .map(d -> d.getPrecio().multiply(BigDecimal.valueOf(d.getCantProd())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
