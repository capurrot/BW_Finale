package it.epicode.bw.finale.fatture;


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
