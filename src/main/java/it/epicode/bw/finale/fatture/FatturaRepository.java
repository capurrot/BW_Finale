package it.epicode.bw.finale.fatture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FatturaRepository extends JpaRepository<Fattura, Long>, JpaSpecificationExecutor<Fattura> {
}
