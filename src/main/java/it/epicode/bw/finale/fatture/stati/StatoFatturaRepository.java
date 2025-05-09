package it.epicode.bw.finale.fatture.stati;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatoFatturaRepository extends JpaRepository<StatoFattura, Long> {
    Optional<StatoFattura> findByNome(String nome);
}
