package it.epicode.bw.finale.indirizzi;

import it.epicode.bw.finale.auth.AppUser;
import it.epicode.bw.finale.auth.Role;
import it.epicode.bw.finale.clienti.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IndirizzoService {

    private final IndirizzoRepository indirizzoRepository;
    private final ComuneRepository comuneRepository;
    private final ClienteRepository clienteRepository;

    private boolean isAdmin(AppUser adminLoggato) {
        boolean isAdmin = adminLoggato.getRoles().contains(Role.ROLE_ADMIN);
        return isAdmin;
    }

    private boolean isUserOrAdmin(AppUser user) {
        return user.getRoles().contains(Role.ROLE_USER) || user.getRoles().contains(Role.ROLE_ADMIN);
    }

    public IndirizzoResponse create(IndirizzoRequest request, AppUser adminLoggato) {
        if (!adminLoggato.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new RuntimeException("Non hai i permessi per creare questo indirizzo");
        }
        Indirizzo indirizzo = toEntity(request);
        return toDTO(indirizzoRepository.save(indirizzo));
    }

    public IndirizzoResponse getById(Long id, AppUser  userLoggato) {
        if (!isUserOrAdmin(userLoggato)) {
            throw new RuntimeException("Non hai i permessi per visualizzare questo indirizzo");
        }
        Indirizzo indirizzo = indirizzoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Indirizzo non trovato"));
        return toDTO(indirizzo);
    }

    public List<IndirizzoResponse> getAll(AppUser  userLoggato) {
        if (!isUserOrAdmin(userLoggato)) {
            throw new RuntimeException("Non hai i permessi per visualizzare questo indirizzo");
        }
        return indirizzoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public IndirizzoResponse update(Long id, IndirizzoRequest request, AppUser  adminLoggato) {
        if (!isAdmin(adminLoggato)) {
            throw new RuntimeException("Non hai i permessi per modificare questo indirizzo");
        }
        Indirizzo indirizzo = indirizzoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Indirizzo non trovato"));

        indirizzo.setVia(request.getVia());
        indirizzo.setCivico(request.getCivico());
        indirizzo.setCap(request.getCap());
        indirizzo.setLocalita(request.getLocalita());
        indirizzo.setTipoSede(request.getTipoSede());
        indirizzo.setComune(comuneRepository.findById(request.getComuneId())
                .orElseThrow(() -> new EntityNotFoundException("Comune non trovato")));
        indirizzo.setCliente(clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente non trovato")));

        return toDTO(indirizzoRepository.save(indirizzo));
    }

    public void delete(Long id, AppUser   adminLoggato) {
        if (!isAdmin(adminLoggato)) {
            throw new RuntimeException("Non hai i permessi per eliminare questo indirizzo");
        }
        indirizzoRepository.deleteById(id);
    }

    private Indirizzo toEntity(IndirizzoRequest request) {
        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setVia(request.getVia());
        indirizzo.setCivico(request.getCivico());
        indirizzo.setCap(request.getCap());
        indirizzo.setLocalita(request.getLocalita());
        indirizzo.setTipoSede(request.getTipoSede());
        indirizzo.setComune(comuneRepository.findById(request.getComuneId())
                .orElseThrow(() -> new EntityNotFoundException("Comune non trovato")));
        indirizzo.setCliente(clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente non trovato")));
        return indirizzo;
    }

    private IndirizzoResponse toDTO(Indirizzo indirizzo) {
        Long clienteId = null;
        if (indirizzo.getCliente() != null) {
            clienteId = indirizzo.getCliente().getId();  // Solo se cliente non è null
        }

        return new IndirizzoResponse(
                indirizzo.getId(),
                indirizzo.getVia(),
                indirizzo.getCivico(),
                indirizzo.getCap(),
                indirizzo.getLocalita(),
                indirizzo.getTipoSede(),
                indirizzo.getComune().getId(),
                clienteId  // Passa clienteId che potrebbe essere null
        );
    }

}
