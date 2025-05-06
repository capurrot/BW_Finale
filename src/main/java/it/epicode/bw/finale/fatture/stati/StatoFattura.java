package it.epicode.bw.finale.fatture.stati;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stati_fatture")

public class StatoFattura {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private  Long id;

    @Column(nullable = false)
    private String nome;


    public StatoFattura(String nome) {
        this.nome = nome;
    }
}
