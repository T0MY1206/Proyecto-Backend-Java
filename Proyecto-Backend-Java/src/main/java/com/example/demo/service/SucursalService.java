package com.example.demo.service;

import com.example.demo.Exception.NotFoundException;
import com.example.demo.dto.SucursalDTO;
import com.example.demo.mapper.Mapper;
import com.example.demo.model.Sucursal;
import com.example.demo.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService implements ISucursalService{

    @Autowired
    private SucursalRepository sucursalRepository;

    @Override
    public List<SucursalDTO> obtenerSucursales() {
        return sucursalRepository.findAll().stream().map(Mapper::toDTO).toList();
    }

    @Override
    public SucursalDTO crearSucursal(SucursalDTO sucursalDTO) {
        Sucursal sucursal = Sucursal.builder()
                .nombre(sucursalDTO.getNombre())
                .direccion(sucursalDTO.getDireccion())
                .build();
        return Mapper.toDTO(sucursalRepository.save(sucursal));
    }

    @Override
    public SucursalDTO actualizarSucursal(Long id, SucursalDTO sucursalDTO) {

        Sucursal sucursal = sucursalRepository.findById(id).orElseThrow(() -> new NotFoundException("Sucursal no encontrado"));
        sucursal.setNombre(sucursalDTO.getNombre());
        sucursal.setDireccion(sucursalDTO.getDireccion());
        return Mapper.toDTO(sucursalRepository.save(sucursal));

    }

    @Override
    public void eliminarSucursal(Long id) {

        if(!sucursalRepository.existsById(id)){
            throw new NotFoundException("Sucursal no encontrado");
        }
        sucursalRepository.deleteById(id);

    }
}
