package it.epicode.bw.finale.clienti;


import it.epicode.bw.finale.enums.TipoCliente;
import it.epicode.bw.finale.indirizzi.Indirizzo;
import it.epicode.bw.finale.indirizzi.IndirizzoRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteRequest {

    private String ragioneSociale;
    private String partitaIva;
    private String email;
    private String dataInserimento;
    private String dataUltimoContatto;
    private String fatturatoAnnuale;
    private String pec;
    private String telefono;
    private String nomeContatto;
    private String cognomeContatto;
    private String emailContatto;
    private String telefonoContatto;
    private String logoAziendale;
    private TipoCliente tipoCliente;
    private Set<IndirizzoRequest> indirizzo;
}
