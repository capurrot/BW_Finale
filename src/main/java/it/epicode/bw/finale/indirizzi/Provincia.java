package it.epicode.bw.finale.indirizzi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "province")

public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private  Long id;
    private String nome;
    private String sigla;

    @OneToMany(mappedBy = "provincia")
    @JsonIgnore
    private List<Comune> comuni = new ArrayList<>();
}


