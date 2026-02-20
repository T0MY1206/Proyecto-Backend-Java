package com.example.demo.service;

import com.example.demo.Exception.NotFoundException;
import com.example.demo.dto.DetalleVentaDTO;
import com.example.demo.dto.VentaDTO;
import com.example.demo.mapper.Mapper;
import com.example.demo.model.DetalleVenta;
import com.example.demo.model.Producto;
import com.example.demo.model.Sucursal;
import com.example.demo.model.Venta;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.SucursalRepository;
import com.example.demo.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService implements IVentaService{

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Override
    public List<VentaDTO> obtenerVentas() {

        List<Venta> ventas = ventaRepository.findAll();
        List<VentaDTO> ventasDTO = new ArrayList<>();
        VentaDTO dto;

        for(Venta venta : ventas){
            dto = Mapper.toDTO(venta);
            ventasDTO.add(dto);
        }

        return ventasDTO;
    }

    @Override
    public VentaDTO crearVenta(VentaDTO ventaDto) {
        if(ventaDto == null) throw new RuntimeException("El venta no puede ser nulo");
        if(ventaDto.getIdSucursal() == null || ventaDto.getDetalle().isEmpty())
            throw new RuntimeException("Debe incluir al menos un producto");

        Sucursal sucursal = sucursalRepository.findById(ventaDto.getIdSucursal()).orElse(null);
        if(sucursal == null) throw new NotFoundException("Sucursal no encontrado");
        Venta venta = new Venta();
        venta.setFecha(ventaDto.getFecha());
        venta.setEstado(ventaDto.getEstado());
        venta.setSucursal(sucursal);
        venta.setTotal(ventaDto.getTotal());

        List<DetalleVenta> detalles = new ArrayList<>();

        Double totalCalculado = 0.0;

        for(DetalleVentaDTO detalleVentaDTO : ventaDto.getDetalle()){
            Producto p = productoRepository.findByNombre(detalleVentaDTO.getNombreProd()).orElse(null);
                    if(p == null) throw new NotFoundException("Producto no encontrado " + detalleVentaDTO.getNombreProd());

            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setProducto(p);
            detalleVenta.setPrecio(detalleVentaDTO.getPrecio());
            detalleVenta.setCantProd(detalleVentaDTO.getCantProd());
            detalleVenta.setVenta(venta);

            detalles.add(detalleVenta);
            totalCalculado = totalCalculado + (detalleVentaDTO.getPrecio() * detalleVentaDTO.getCantProd());
        }

        venta.setDetalle(detalles);

        venta = ventaRepository.save(venta);

        return Mapper.toDTO(venta);
    }

    @Override
    public VentaDTO actualizarVenta(Long id, VentaDTO ventaDto) {

        Venta venta = ventaRepository.findById(id).orElse(null);
        if(venta == null) throw new NotFoundException("Venta no encontrado");

        if(ventaDto.getFecha() != null) venta.setFecha(ventaDto.getFecha());
        if(ventaDto.getEstado() != null) venta.setEstado(ventaDto.getEstado());
        if(ventaDto.getTotal() != null) venta.setTotal(ventaDto.getTotal());
        if(ventaDto.getIdSucursal() != null){
            Sucursal sucursal = sucursalRepository.findById(ventaDto.getIdSucursal()).orElse(null);
            if(sucursal == null) throw new NotFoundException("Sucursal no encontrado");
            venta.setSucursal(sucursal);
        }

        return Mapper.toDTO(ventaRepository.save(venta));

    }

    @Override
    public void eliminarVenta(Long id) {
        Venta venta = ventaRepository.findById(id).orElse(null);
        if(venta == null) throw new NotFoundException("Venta no encontrado");
        ventaRepository.delete(venta);
    }
}
