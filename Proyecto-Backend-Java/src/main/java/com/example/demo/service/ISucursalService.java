package com.example.demo.service;

import com.example.demo.dto.SucursalDTO;
import java.util.List;

public interface ISucursalService {

    List<SucursalDTO> obtenerSucursales();
    SucursalDTO crearSucursal(SucursalDTO sucursalDTO);
    SucursalDTO actualizarSucursal(Long id, SucursalDTO sucursalDTO);
    void eliminarSucursal(Long id);
}
