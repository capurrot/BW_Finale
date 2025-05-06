package it.epicode.bw.finale.indirizzi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndirizzoResponse {
    private Long id;
    private String via;
    private String civico;
    private String cap;
    private String localita;
    private TipoSede tipoSede;
    private Long comuneId;
    private Long clienteId;
}
