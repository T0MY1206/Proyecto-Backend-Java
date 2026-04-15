package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.dto.DetalleVentaDTO;
import com.example.demo.dto.VentaDTO;
import com.example.demo.mapper.VentaMapper;
import com.example.demo.model.DetalleVenta;
import com.example.demo.model.Producto;
import com.example.demo.model.Sucursal;
import com.example.demo.model.Venta;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.SucursalRepository;
import com.example.demo.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VentaService implements IVentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final VentaMapper ventaMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<VentaDTO> obtenerVentas(Pageable pageable) {
        return ventaRepository.findAll(pageable)
                .map(ventaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public VentaDTO obtenerVentaPorId(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada"));
        return ventaMapper.toDTO(venta);
    }

    @Override
    @Transactional
    public VentaDTO crearVenta(VentaDTO ventaDto) {
        log.info("Creando venta para sucursal id={}", ventaDto.getIdSucursal());
        Sucursal sucursal = sucursalRepository.findById(ventaDto.getIdSucursal())
                .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada"));

        Venta venta = new Venta();
        venta.setFecha(ventaDto.getFecha());
        venta.setEstado(ventaDto.getEstado());
        venta.setSucursal(sucursal);

        List<DetalleVenta> detalles = new ArrayList<>();
        BigDecimal totalCalculado = BigDecimal.ZERO;

        for (DetalleVentaDTO detalleDTO : ventaDto.getDetalle()) {
            Producto producto = productoRepository.findByNombre(detalleDTO.getNombreProd())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Producto no encontrado: " + detalleDTO.getNombreProd()));

            DetalleVenta detalle = DetalleVenta.builder()
                    .producto(producto)
                    .precio(detalleDTO.getPrecio())
                    .cantProd(detalleDTO.getCantProd())
                    .venta(venta)
                    .build();

            detalles.add(detalle);
            totalCalculado = totalCalculado.add(
                    detalleDTO.getPrecio().multiply(BigDecimal.valueOf(detalleDTO.getCantProd()))
            );
        }

        venta.setDetalle(detalles);
        venta.setTotal(totalCalculado);

        return ventaMapper.toDTO(ventaRepository.save(venta));
    }

    @Override
    @Transactional
    public VentaDTO actualizarVenta(Long id, VentaDTO ventaDto) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada"));

        if (ventaDto.getFecha() != null) venta.setFecha(ventaDto.getFecha());
        if (ventaDto.getEstado() != null) venta.setEstado(ventaDto.getEstado());

        if (ventaDto.getIdSucursal() != null) {
            Sucursal sucursal = sucursalRepository.findById(ventaDto.getIdSucursal())
                    .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada"));
            venta.setSucursal(sucursal);
        }

        return ventaMapper.toDTO(ventaRepository.save(venta));
    }

    @Override
    @Transactional
    public void eliminarVenta(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada"));
        ventaRepository.delete(venta);
        log.info("Venta eliminada: id={}", id);
    }
}
