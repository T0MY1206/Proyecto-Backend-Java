package com.example.demo.service;

import com.example.demo.dto.SucursalDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISucursalService {

    Page<SucursalDTO> obtenerSucursales(Pageable pageable);

    SucursalDTO obtenerSucursalPorId(Long id);

    SucursalDTO crearSucursal(SucursalDTO sucursalDTO);

    SucursalDTO actualizarSucursal(Long id, SucursalDTO sucursalDTO);

    void eliminarSucursal(Long id);
}
