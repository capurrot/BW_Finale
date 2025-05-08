package it.epicode.bw.finale;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.epicode.bw.finale.auth.*;

import it.epicode.bw.finale.clienti.Cliente;
import it.epicode.bw.finale.clienti.ClienteRequest;
import it.epicode.bw.finale.clienti.ClienteResponse;
import it.epicode.bw.finale.enums.TipoCliente;
import it.epicode.bw.finale.indirizzi.IndirizzoRequest;
import it.epicode.bw.finale.indirizzi.TipoSede;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClienteControllerTest {

    @LocalServerPort
    public int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppUserRepository appUserRepository;

    private static String tokenNormale;
    private static String tokenAdmin;


    // creazione non funziona perchè c'è un problema quando inserisci indirizzo in cliente (indirizzo ha clienteid che ancora non esiste se non posso dargli indirizzo)


    @Test
    @Order(1)
    @DisplayName("Test di registrazione admin")
    public void testRegisterAdmin() {
        System.out.println("Inizio test: registrazione admin");
        AppUser appUser = new AppUser();
        appUser.setUsername("admin.test");
        appUser.setPassword(passwordEncoder.encode("adminpwd"));
        appUser.setRoles(Set.of(Role.ROLE_ADMIN));
        appUserRepository.save(appUser);
        System.out.println("Admin salvato nel repository");

        LoginRequest loginRequest = new LoginRequest("admin.test", "adminpwd");
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity("http://localhost:"+ port +"/login", loginRequest, AuthResponse.class);
        tokenAdmin = response.getBody().getToken();
        System.out.println("Token ottenuto: " + tokenAdmin);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenAdmin);
        RegisterRequest registerRequest = new RegisterRequest("admin.test2", "password", "admin@example.com", "Admin", "Test");
        HttpEntity<RegisterRequest> entity = new HttpEntity<>(registerRequest, headers);
        ResponseEntity<String> response2 = restTemplate.postForEntity("http://localhost:"+ port +"/register-admin", entity, String.class);
        System.out.println("Risposta registrazione nuovo admin: " + response2.getStatusCode());
        assertThat(response2.getStatusCodeValue()).isEqualTo(200);
    }
    @Test
    @Order(2)
    @DisplayName("Test di creazione cliente")
    public void testCreateCliente() {
        System.out.println("Inizio test: creazione cliente");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenAdmin);

        ClienteRequest clienteRequest = new ClienteRequest();
        clienteRequest.setRagioneSociale("Azienda S.p.A.");
        clienteRequest.setPartitaIva("IT12345678901");
        clienteRequest.setEmail("azienda@example.com");
        clienteRequest.setDataInserimento("2025-05-08");
        clienteRequest.setDataUltimoContatto("2025-05-07");
        clienteRequest.setFatturatoAnnuale("50000.0");
        clienteRequest.setPec("azienda@pec.com");
        clienteRequest.setTelefono("+390123456789");
        clienteRequest.setNomeContatto("Mario");
        clienteRequest.setCognomeContatto("Rossi");
        clienteRequest.setEmailContatto("mario.rossi@example.com");
        clienteRequest.setTelefonoContatto("+390987654321");
        clienteRequest.setLogoAziendale("http://example.com/logo.png");
        clienteRequest.setTipoCliente(TipoCliente.PA);

        // Creazione dell'oggetto IndirizzoRequest
        IndirizzoRequest request = new IndirizzoRequest("Via dalle palle", "35", "95030", "Balneare",
                TipoSede.SEDE_OPERATIVA, 1L, null);
        // Impostazione dell'indirizzo nel clienteRequest
        clienteRequest.setIndirizzo(Set.of(request)); // Corretta la chiusura della parentesi

        // Effettua la richiesta HTTP
        HttpEntity<ClienteRequest> entity = new HttpEntity<>(clienteRequest, headers);
        ResponseEntity<Cliente> response = restTemplate.exchange("http://localhost:" + port + "/clienti",
                HttpMethod.POST, entity, Cliente.class);

        System.out.println("Risposta creazione cliente: " + response.getStatusCode());
        assertThat(response.getStatusCodeValue()).isEqualTo(201);  // Stato CREATED
    }

    @Test
    @Order(3)
    @DisplayName("Test di recupero cliente per ID")
    public void testGetClienteById() {
        // Crea un cliente e poi lo recupera per id
        Long clienteId = 1L; // supponiamo che l'ID del cliente appena creato sia 1
        ResponseEntity<ClienteResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/clienti/" + clienteId,
                HttpMethod.GET,
                new HttpEntity<>(null, createHeaders()),
                ClienteResponse.class
        );

        System.out.println("Risposta recupero cliente per ID: " + response.getStatusCode());
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @Order(4)
    @DisplayName("Test di aggiornamento cliente")
    public void testUpdateCliente() {
        Long clienteId = 1L; // Supponiamo che il cliente ID 1 esista
        ClienteRequest clienteRequest = new ClienteRequest();
        clienteRequest.setNomeContatto("Mario Rossi Updated");
        clienteRequest.setFatturatoAnnuale("60000.0");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenAdmin);

        HttpEntity<ClienteRequest> entity = new HttpEntity<>(clienteRequest, headers);
        ResponseEntity<ClienteResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/clienti/" + clienteId,
                HttpMethod.PUT,
                entity,
                ClienteResponse.class
        );

        System.out.println("Risposta aggiornamento cliente: " + response.getStatusCode());
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().getNomeContatto()).isEqualTo("Mario Rossi Updated");
    }

    @Test
    @Order(5)
    @DisplayName("Test di cancellazione cliente")
    public void testDeleteCliente() {
        Long clienteId = 1L; // Supponiamo che il cliente ID 1 esista
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/clienti/" + clienteId,
                HttpMethod.DELETE,
                new HttpEntity<>(null, createHeaders()),
                Void.class
        );

        System.out.println("Risposta cancellazione cliente: " + response.getStatusCode());
        assertThat(response.getStatusCodeValue()).isEqualTo(204);  // No content
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        return headers;
    }
}


