package it.epicode.bw.finale.fatture;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FatturaRequest {

    @NotNull(message = "La data è obbligatoria")
    private LocalDate data;

    @NotNull(message = "L'importo è obbligatorio")
    @Positive(message = "L'importo deve essere positivo")
    private Double importo;

    @NotNull(message = "Il numero fattura è obbligatorio")
    @Min(value = 1, message = "Il numero deve essere almeno 1")
    private int numero;

    @NotNull(message = "Devi specificare il cliente")
    private Long clienteId;

    @NotNull(message = "Lo stato è obbligatorio")
    private StatoFattura stato;
}
