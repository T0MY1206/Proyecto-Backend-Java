package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDTO {

    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El estado es obligatorio")
    private String estado;

    @NotNull(message = "El ID de sucursal es obligatorio")
    private Long idSucursal;

    @NotEmpty(message = "Debe incluir al menos un producto en el detalle")
    @Valid
    private List<DetalleVentaDTO> detalle;

    private BigDecimal total;
}
