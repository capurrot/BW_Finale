package it.epicode.bw.finale.fatture;


import it.epicode.bw.finale.fatture.stati.StatoFattura;


import java.time.LocalDate;

public record FatturaResponse(
        Long id,
        LocalDate data,
        Double importo,
        Integer numero,
        Long clienteId,
        StatoFattura stato
) {
}
