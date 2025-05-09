package it.epicode.bw.finale.indirizzi;

import com.github.javafaker.App;
import it.epicode.bw.finale.auth.AppUser;
import it.epicode.bw.finale.auth.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComuneService {

    private final ComuneRepository comuneRepository;
    private final ProvinciaRepository provinciaRepository;

    private boolean isAdmin(AppUser adminLoggato) {
        boolean isAdmin = adminLoggato.getRoles().contains(Role.ROLE_ADMIN);
        return isAdmin;
    }


        public ComuneResponse create(ComuneRequest request, AppUser adminLoggato) {
            if (!isAdmin(adminLoggato)) {
                throw new RuntimeException("Non hai i permessi per creare questo comune");
            }

        Comune comune = new Comune();
        comune.setNome(request.getNome());
        comune.setProvincia(provinciaRepository.findById(request.getProvinciaId())
                .orElseThrow(() -> new EntityNotFoundException("Provincia non trovata")));
        return toDTO(comuneRepository.save(comune));
    }

    public ComuneResponse getById(Long id) {
        Comune comune = comuneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comune non trovato"));
        return toDTO(comune);
    }

    public Page<ComuneResponse> getAll(Pageable pageable) {
        return comuneRepository.findAll(pageable)
                .map(this::toDTO);  // Usa il mapping dal tuo Entity alla DTO
    }


    public ComuneResponse update(Long id, ComuneRequest request,AppUser adminLoggato) {
        if (!isAdmin(adminLoggato)) {
            throw new RuntimeException("Non hai i permessi per modificare questo comune");
        }
        Comune comune = comuneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comune non trovato"));
        comune.setNome(request.getNome());
        comune.setProvincia(provinciaRepository.findById(request.getProvinciaId())
                .orElseThrow(() -> new EntityNotFoundException("Provincia non trovata")));
        return toDTO(comuneRepository.save(comune));
    }

    public void delete(Long id, AppUser  adminLoggato) {
        if (!isAdmin(adminLoggato)) {
            throw new RuntimeException("Non hai i permessi per eliminare questo comune");
        }
        comuneRepository.deleteById(id);
    }

    private ComuneResponse toDTO(Comune comune) {
        return new ComuneResponse(comune.getId(), comune.getNome(), comune.getProvincia().getId());
    }
}
