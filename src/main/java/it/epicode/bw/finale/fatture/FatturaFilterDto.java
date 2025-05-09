package it.epicode.bw.finale.fatture;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FatturaFilterDto {
    private Long idCliente;
    private LocalDate data;
    private Integer anno;
    private Double importoDa;
    private Double importoA;
}
