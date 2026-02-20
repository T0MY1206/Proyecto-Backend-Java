package com.example.demo.mapper;

import com.example.demo.dto.DetalleVentaDTO;
import com.example.demo.dto.ProductoDTO;
import com.example.demo.dto.SucursalDTO;
import com.example.demo.dto.VentaDTO;
import com.example.demo.model.Producto;
import com.example.demo.model.Sucursal;
import com.example.demo.model.Venta;

import java.util.stream.Collectors;

public class Mapper {

    public static ProductoDTO toDTO(Producto p) {

        if(p == null) return null;

        return ProductoDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .categoria(p.getCategoria())
                .precio(p.getPrecio())
                .cantidad(p.getCantidad())
                .build();

    }

    public static VentaDTO toDTO(Venta v) {

        if(v == null) return null;

        var detalle = v.getDetalle().stream().map(det ->
                DetalleVentaDTO.builder()
                        .id(det.getProducto().getId())
                        .nombreProd(det.getProducto().getNombre())
                        .cantProd(det.getCantProd())
                        .precio(det.getPrecio())
                        .subtotal(det.getPrecio() * det.getCantProd())
                        .build()
        ).toList();

        var total = detalle.stream()
                .map(DetalleVentaDTO::getSubtotal)
                .reduce(0.0, Double::sum);

        return VentaDTO.builder()
                .id(v.getId())
                .fecha(v.getFecha())
                .IdSucursal(v.getSucursal().getId())
                .estado(v.getEstado())
                .detalle(detalle)
                .total(total)
                .build();
    }

    public static SucursalDTO toDTO(Sucursal s) {

        if(s == null) return null;

        return SucursalDTO.builder()
                .id(s.getId())
                .nombre(s.getNombre())
                .direccion(s.getDireccion())
                .build();
    }

}
