package it.epicode.bw.finale.filter;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClienteOrderDto {
    private String ragioneSociale;
    private Double fatturatoAnnuale;
    private LocalDate dataInserimento;
    private LocalDate dataUltimoContatto;
    private String provincia;
}
