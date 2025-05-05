package it.epicode.bw.finale.indirizzi;

import lombok.Data;

@Data
public class IndirizzoResponse {
    private Long id;
    private String via;
    private String civico;
    private String localita;
    private String cap;
    private ComuneResponse comune;
}
