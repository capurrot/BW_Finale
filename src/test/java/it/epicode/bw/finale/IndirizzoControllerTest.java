package it.epicode.bw.finale;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.epicode.bw.finale.auth.*;
import it.epicode.bw.finale.indirizzi.IndirizzoRequest;
import it.epicode.bw.finale.indirizzi.IndirizzoResponse;
import it.epicode.bw.finale.indirizzi.TipoSede;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IndirizzoControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AppUserRepository appUserRepository;

    private static String tokenAdmin;
    private static Long indirizzoCreatoId;

    @BeforeAll
    public static void clearDatabase() {
        System.out.println("Cancellazione database");
    }


    @Test
    @Order(1)
    @DisplayName("Login e registrazione admin")
    public void testRegisterAdmin() {
        AppUser appUser = new AppUser();
        appUser.setUsername("admin.test");
        appUser.setPassword(passwordEncoder.encode("adminpwd"));
        appUser.setRoles(Set.of(Role.ROLE_ADMIN));
        appUserRepository.save(appUser);

        LoginRequest loginRequest = new LoginRequest("admin.test", "adminpwd");
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/login", loginRequest, AuthResponse.class);
        tokenAdmin = response.getBody().getToken();

        System.out.println("Token ottenuto: " + tokenAdmin);
        assertThat(tokenAdmin).isNotNull();
    }

    @Test
    @Order(2)
    @DisplayName("Crea indirizzo")
    public void testCreateIndirizzo() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenAdmin);

        IndirizzoRequest request = new IndirizzoRequest("Via dalle palle","35","95030","Balneare", TipoSede.SEDE_OPERATIVA, 1L,10L);
        HttpEntity<IndirizzoRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<IndirizzoResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/indirizzi", entity, IndirizzoResponse.class);
        indirizzoCreatoId = response.getBody().getId();

        System.out.println("Indirizzo creato con ID: " + indirizzoCreatoId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @Order(3)
    @DisplayName("Recupera indirizzo per ID")
    public void testGetIndirizzoById() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<IndirizzoResponse> response = restTemplate.exchange("http://localhost:" + port + "/indirizzi/" + indirizzoCreatoId, HttpMethod.GET, entity, IndirizzoResponse.class);

        System.out.println("Indirizzo recuperato: " + response.getBody().getVia());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(4)
    @DisplayName("Aggiorna indirizzo")
    public void testUpdateIndirizzo() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenAdmin);

        IndirizzoRequest updatedRequest = new IndirizzoRequest("Via update","Update35","Update95030","UpdateBalneare", TipoSede.SEDE_OPERATIVA, 2L,10L);
        HttpEntity<IndirizzoRequest> entity = new HttpEntity<>(updatedRequest, headers);

        ResponseEntity<IndirizzoResponse> response = restTemplate.exchange("http://localhost:" + port + "/indirizzi/" + indirizzoCreatoId, HttpMethod.PUT, entity, IndirizzoResponse.class);

        System.out.println("Indirizzo aggiornato: " + response.getBody().getVia());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(5)
    @DisplayName("Recupera tutti gli indirizzi")
    public void testGetAllIndirizzi() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/indirizzi", HttpMethod.GET, entity, String.class);

        System.out.println("Risposta tutti indirizzi: " + response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Order(6)
    @DisplayName("Cancella indirizzo")
    public void testDeleteIndirizzo() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:" + port + "/indirizzi/" + indirizzoCreatoId, HttpMethod.DELETE, entity, Void.class);

        System.out.println("Indirizzo con ID " + indirizzoCreatoId + " eliminato.");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
