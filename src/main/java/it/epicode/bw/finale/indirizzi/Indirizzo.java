package it.epicode.bw.finale.indirizzi;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.bw.finale.clienti.Cliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "indirizzi",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"cliente_id", "tipoSede"})
        }
)

@EqualsAndHashCode(exclude = "cliente")
public class Indirizzo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private  Long id;
    private String via;
    private String civico;
    private String cap;
    private String localita;
    @Enumerated(EnumType.STRING)
    private TipoSede tipoSede;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonBackReference
    @JsonIgnore
    private Cliente cliente;


    @ManyToOne
    @JoinColumn(name="comune_id")
    private Comune comune;


}