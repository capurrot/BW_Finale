package it.epicode.bw.finale.indirizzi;

import it.epicode.bw.finale.auth.AppUser;
import it.epicode.bw.finale.auth.Role;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinciaService {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    private boolean isAdmin(AppUser adminLoggato) {
        return adminLoggato.getRoles().contains(Role.ROLE_ADMIN);
    }

    public ProvinciaResponse create(ProvinciaRequest request, AppUser adminLoggato) {
        if (!isAdmin(adminLoggato)) {
            throw new RuntimeException("Non hai i permessi per creare questa provincia");
        }
        Provincia provincia = new Provincia();
        provincia.setNome(request.getNome());
        provincia.setSigla(request.getSigla());
        provincia = provinciaRepository.save(provincia);
        return toDTO(provincia);
    }

    public ProvinciaResponse getById(Long id) {
        Provincia provincia = provinciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provincia non trovata"));
        return toDTO(provincia);
    }

    public Page<ProvinciaResponse> getAll(Pageable pageable) {
        return provinciaRepository.findAll(pageable)
                .map(this::toDTO);

    }

    public ProvinciaResponse update(Long id, ProvinciaRequest request, AppUser adminLoggato) {
        if (!isAdmin(adminLoggato)) {
            throw new RuntimeException("Non hai i permessi per modificare questa provincia");
        }
        Provincia provincia = provinciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Provincia non trovata"));
        provincia.setNome(request.getNome());
        provincia.setSigla(request.getSigla());
        return toDTO(provinciaRepository.save(provincia));
    }

    public void delete(Long id, AppUser adminLoggato) {
        if (!isAdmin(adminLoggato)) {
            throw new RuntimeException("Non hai i permessi per eliminare questa provincia");
        }
        provinciaRepository.deleteById(id);
    }

    private ProvinciaResponse toDTO(Provincia provincia) {
        return new ProvinciaResponse(provincia.getId(), provincia.getNome(), provincia.getSigla());
    }
}
