package it.epicode.bw.finale.clienti;

import it.epicode.bw.finale.enums.TipoCliente;

import java.time.LocalDate;

public class ClienteResponse {

    private Long id;
    private String ragioneSociale;
    private String partitaIva;
    private String email;
    private LocalDate dataInserimento;
    private LocalDate dataUltimoContatto;
    private double fatturatoAnnuale;
    private String pec;
    private String telefono;
    private String nomeContatto;
    private String cognomeContatto;
    private String emailContatto;
    private String telefonoContatto;
    private String logoAziendale;
    private TipoCliente tipoCliente;
}
