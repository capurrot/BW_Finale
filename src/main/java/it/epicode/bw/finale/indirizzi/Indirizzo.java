package it.epicode.bw.finale.indirizzi;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "indirizzi")

public class Indirizzo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private  Long id;
    private String via;
    private String civico;
    private String cap;
    private String localita;

    @ManyToOne
    @JoinColumn(name="comune_id")
    private Comune comune;


}