package it.epicode.bw.finale.utenti;

import it.epicode.bw.finale.auth.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Utenti")

public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String email;
    private String nome;
    private String cognome;
    private String avatar;

    @OneToOne(cascade={CascadeType.REMOVE, CascadeType.PERSIST})
    private AppUser appUser;

}