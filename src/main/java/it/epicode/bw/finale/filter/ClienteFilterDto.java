package it.epicode.bw.finale.filter;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClienteFilterDto {
    private Double fatturatoAnnuale;
    private Double fatturatoMax;
    private LocalDate dataInserimentoDa;
    private LocalDate dataInserimentoA;
    private LocalDate dataUltimoContattoDa;
    private LocalDate dataUltimoContattoA;
    private String provincia;
}
