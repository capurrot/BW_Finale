package it.epicode.bw.finale.indirizzi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComuneResponse {
    private Long id;
    private String nome;
    private Long provinciaId;
}
