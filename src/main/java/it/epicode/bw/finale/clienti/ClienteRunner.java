package it.epicode.bw.finale.clienti;

import com.github.javafaker.Faker;
import it.epicode.bw.finale.enums.TipoCliente;
import it.epicode.bw.finale.indirizzi.Comune;
import it.epicode.bw.finale.indirizzi.Indirizzo;
import it.epicode.bw.finale.indirizzi.Provincia;
import it.epicode.bw.finale.indirizzi.TipoSede;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
@Component
public class ClienteRunner implements CommandLineRunner {
    @Autowired
    Faker faker;
    @Autowired
    ClienteRepository clienteRepository;
    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 100; i++) {



        }
        for (int i = 0; i < 10; i++) {
            String nome = faker.name().firstName();
            String cognome = faker.name().lastName();
            String compagnia = faker.company().name();
            Cliente cliente = new Cliente();
            Provincia provincia = new Provincia();
            Comune comune = new Comune();
            Indirizzo indirizzo = new Indirizzo();
            provincia.setNome(faker.address().city());
            provincia.setSigla(faker.address().stateAbbr());
            comune.setNome(faker.address().cityName());
            comune.setProvincia(provincia);
            cliente.setNomeContatto(nome);
            cliente.setCognomeContatto(cognome);
            cliente.setRagioneSociale(compagnia);
            cliente.setPartitaIva(faker.number().digits(11));
            cliente.setEmailContatto((nome + cognome + "@gmail.com").toLowerCase());
            cliente.setEmail((nome + cognome + "@gmail.com").toLowerCase());
            cliente.setTelefono(faker.phoneNumber().phoneNumber());
            cliente.setTelefonoContatto(faker.phoneNumber().phoneNumber());
            cliente.setPec((nome + cognome + "@gmail.com").toLowerCase());
            cliente.setTipoCliente(TipoCliente.valueOf(faker.options().option("SAS", "SRL", "SPA", "PA")));
            cliente.setFatturatoAnnuale(faker.number().randomDouble(2, 10000, 100000));
            cliente.setLogoAziendale("https://ui-avatars.com/api/?name=" + compagnia);
            cliente.setDataInserimento(LocalDate.now());
            cliente.setDataUltimoContatto(LocalDate.now());
            indirizzo.setVia(faker.address().streetName());
            indirizzo.setCivico(faker.address().streetAddressNumber());
            indirizzo.setCap(faker.address().zipCode());
            indirizzo.setLocalita(faker.address().city());
            indirizzo.setComune(comune);
            indirizzo.setTipoSede(TipoSede.valueOf(faker.options().option("SEDE_LEGALE", "SEDE_OPERATIVA")));
            cliente.setIndirizzo(Set.of(indirizzo));
            clienteRepository.save(cliente);

        }

    }
}
