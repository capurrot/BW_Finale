package it.epicode.bw.finale.fatture;

import it.epicode.bw.finale.clienti.Cliente;
import it.epicode.bw.finale.fatture.stati.StatoFattura;
import it.epicode.bw.finale.fatture.stati.StatoFatturaRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fatture")

public class Fattura {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column( nullable = false)
    private LocalDate data;
    @Column( nullable = false)
    private Double importo;
    @Column( nullable = false)
    private int numero;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne @JoinColumn(name ="stato", nullable = false)
    private StatoFattura stato;

}
