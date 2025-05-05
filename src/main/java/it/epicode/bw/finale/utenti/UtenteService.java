package it.epicode.bw.finale.utenti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepository utenteRepository;
    public Utente getUtentebyId(long id) {
        return utenteRepository.findById(id).orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + id));
    }
    public List<Utente> getAllUtenti() {return utenteRepository.findAll();}
    public Utente findByUsername(String username) {return utenteRepository.findByAppUserUsername(username);}
}
