package com.example.demo.service;

import com.example.demo.dto.VentaDTO;

import java.util.List;

public interface IVentaService {
    List<VentaDTO> obtenerVentas();
    VentaDTO crearVenta(VentaDTO venta);
    VentaDTO actualizarVenta(Long id, VentaDTO venta);
    void eliminarVenta(Long id);
}
