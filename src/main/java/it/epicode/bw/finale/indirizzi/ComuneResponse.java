package it.epicode.bw.finale.indirizzi;

import lombok.Data;

@Data
public class ComuneResponse {
    private Long id;
    private String nome;
    private ProvinciaResponse provincia;
}
