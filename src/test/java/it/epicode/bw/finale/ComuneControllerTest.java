package it.epicode.bw.finale;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.epicode.bw.finale.auth.*;
import it.epicode.bw.finale.indirizzi.ComuneRequest;
import it.epicode.bw.finale.indirizzi.ComuneResponse;
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
public class ComuneControllerTest {

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
    private static Long comuneCreatoId;

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
    public void testCreateComune() {
        System.out.println("Inizio test: creazione comune");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ComuneRequest request = new ComuneRequest("Zeneix", 1L);
        HttpEntity<ComuneRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ComuneResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/comuni", entity, ComuneResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNome()).isEqualTo("Zeneix");
        comuneCreatoId = response.getBody().getId();

        System.out.println("Comune creato con ID: " + comuneCreatoId);
        System.out.println("Comune creato con nome: " + response.getBody().getNome());
    }

    @Test
    @Order(3)
    @DisplayName("Test di recupero comuni con paginazione")
    public void testGetAllComuni() {
        System.out.println("Inizio test: recupero comuni con paginazione");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "http://localhost:" + port + "/comuni?page=0&size=5";

        ResponseEntity<RestPageImpl<ComuneResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<RestPageImpl<ComuneResponse>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotalElements()).isGreaterThan(0);

        System.out.println("Numero totale comuni trovati: " + response.getBody().getTotalElements());
        System.out.println("Numero pagina ricevuta: " + response.getBody().getPageable().getPageNumber());
    }

    @Test
    @Order(4)
    public void testGetComuneById() {
        System.out.println("Inizio test: recupero comune per ID");
        ResponseEntity<ComuneResponse> response = restTemplate.getForEntity("http://localhost:" + port + "/comuni/" + comuneCreatoId, ComuneResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNome()).isEqualTo("Zeneix");

        System.out.println("Comune recuperato con nome: " + response.getBody().getNome());
    }

    @Test
    @Order(5)
    public void testUpdateComune() {
        System.out.println("Inizio test: aggiornamento comune");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ComuneRequest request = new ComuneRequest("Zeneix Aggiornato", 1L);
        HttpEntity<ComuneRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ComuneResponse> response = restTemplate.exchange(
                "http://localhost:" + port + "/comuni/" + comuneCreatoId,
                HttpMethod.PUT,
                entity,
                ComuneResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getNome()).isEqualTo("Zeneix Aggiornato");

        System.out.println("Comune aggiornato: " + response.getBody().getNome());
    }

    @Test
    @Order(6)
    public void testDeleteComune() {
        System.out.println("Inizio test: cancellazione comune");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenAdmin);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/comuni/" + comuneCreatoId,
                HttpMethod.DELETE,
                entity,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        System.out.println("Comune cancellato con successo (ID: " + comuneCreatoId + ")");
    }
}
