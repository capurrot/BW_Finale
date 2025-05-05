package it.epicode.bw.finale.clienti;

import it.epicode.bw.finale.enums.TipoCliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clienti")

public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String ragioneSociale;

    @Column(unique = true, nullable = false, length = 100)
    private String partitaIva;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    private LocalDate dataInserimento = LocalDate.now();
    private LocalDate dataUltimoContatto;
    private double fatturatoAnnuale;

    @Column(unique = true, nullable = false, length = 100)
    private String pec;
    private String telefono;

    @Column(length = 100)
    private String nomeContatto;
    @Column(length = 100)
    private String cognomeContatto;

    @Column(unique = true, length = 100)
    private String emailContatto;

    private String telefonoContatto;

    private String logoAziendale;

    //private Indirizzi indirizzi;

    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente;

}