package it.epicode.bw.finale.fatture.stati;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatoFatturaRequest {

    @NotBlank(message = "Il nome dello stato è obbligatorio")
    private String nome;

    public StatoFatturaRequest(String nome) {
        this.nome = nome;
    }
}
