package com.example.demo.service;

import com.example.demo.dto.VentaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IVentaService {

    Page<VentaDTO> obtenerVentas(Pageable pageable);

    VentaDTO obtenerVentaPorId(Long id);

    VentaDTO crearVenta(VentaDTO venta);

    VentaDTO actualizarVenta(Long id, VentaDTO venta);

    void eliminarVenta(Long id);
}
