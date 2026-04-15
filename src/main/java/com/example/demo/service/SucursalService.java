package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.dto.SucursalDTO;
import com.example.demo.mapper.SucursalMapper;
import com.example.demo.model.Sucursal;
import com.example.demo.repository.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SucursalService implements ISucursalService {

    private final SucursalRepository sucursalRepository;
    private final SucursalMapper sucursalMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SucursalDTO> obtenerSucursales(Pageable pageable) {
        return sucursalRepository.findAll(pageable)
                .map(sucursalMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalDTO obtenerSucursalPorId(Long id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada"));
        return sucursalMapper.toDTO(sucursal);
    }

    @Override
    @Transactional
    public SucursalDTO crearSucursal(SucursalDTO sucursalDTO) {
        log.info("Creando sucursal: {}", sucursalDTO.getNombre());
        Sucursal sucursal = sucursalMapper.toEntity(sucursalDTO);
        return sucursalMapper.toDTO(sucursalRepository.save(sucursal));
    }

    @Override
    @Transactional
    public SucursalDTO actualizarSucursal(Long id, SucursalDTO sucursalDTO) {
        Sucursal sucursal = sucursalRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada"));

        sucursal.setNombre(sucursalDTO.getNombre());
        sucursal.setDireccion(sucursalDTO.getDireccion());

        return sucursalMapper.toDTO(sucursalRepository.save(sucursal));
    }

    @Override
    @Transactional
    public void eliminarSucursal(Long id) {
        if (!sucursalRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sucursal no encontrada");
        }
        sucursalRepository.deleteById(id);
        log.info("Sucursal eliminada: id={}", id);
    }
}
