package it.epicode.bw.finale.filter;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FattureFilterDto {
    private Long idCliente;
    private LocalDate data;
    private LocalDate anno;
    private Double importoDa;
    private Double importoA;
}
