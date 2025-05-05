package it.epicode.bw.finale.clienti;


import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByEmail(String email);

    boolean existsByPartitaIva(String partitaIva);

    boolean existsByPec(String pec);

    boolean existsByTelefono(String telefono);

    boolean existsByRagioneSociale(String ragioneSociale);
}