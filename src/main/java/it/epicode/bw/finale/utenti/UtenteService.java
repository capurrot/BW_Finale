package it.epicode.bw.finale.utenti;

import it.epicode.bw.finale.common.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UtenteService {
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    public Utente getUtentebyId(long id) {
        return utenteRepository.findById(id).orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + id));
    }
    public List<Utente> getAllUtenti() {return utenteRepository.findAll();}
    public Utente findByUsername(String username) {return utenteRepository.findByAppUserUsername(username);}
    public void uploadAvatar(long id, MultipartFile file) {
        Utente utente = getUtentebyId(id);
        utente.setAvatar(cloudinaryService.uploadImage(file));
        utenteRepository.save(utente);
    }
}
