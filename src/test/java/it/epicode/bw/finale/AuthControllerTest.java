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
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest {

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
    @DisplayName("Test di login utente")
    public void testLoginUser() {
        System.out.println("Inizio test: login utente");
        AppUser appUser = new AppUser();
        appUser.setUsername("gianni.bugno");
        appUser.setPassword(passwordEncoder.encode("password"));
        appUser.setRoles(Set.of(Role.ROLE_USER));
        appUserRepository.save(appUser);
        System.out.println("Utente salvato: gianni.bugno");

        LoginRequest loginRequest = new LoginRequest("gianni.bugno", "password");
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/login", loginRequest, String.class);
        tokenNormale = response.getBody();

        System.out.println("Risposta login status: " + response.getStatusCode());
        System.out.println("Token ricevuto: " + tokenNormale);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @Order(2)
    @DisplayName("Test di registrazione utente")
    public void testRegisterUser() {
        System.out.println("Inizio test: registrazione utente");
        RegisterRequest registerRequest = new RegisterRequest("giorgio.mastrota", "password", "mondialcasa@example.com", "Giorgio", "Mastrota");
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/register", registerRequest, String.class);

        System.out.println("Risposta registrazione status: " + response.getStatusCode());
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @Order(3)
    @DisplayName("Test di registrazione admin")
    public void testRegisterAdmin() {
        System.out.println("Inizio test: registrazione admin");

        AppUser appUser = new AppUser();
        appUser.setUsername("admin.test");
        appUser.setPassword(passwordEncoder.encode("adminpwd"));
        appUser.setRoles(Set.of(Role.ROLE_ADMIN));
        appUserRepository.save(appUser);
        System.out.println("Admin salvato: admin.test");

        LoginRequest loginRequest = new LoginRequest("admin.test", "adminpwd");
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity("http://localhost:" + port + "/login", loginRequest, AuthResponse.class);
        tokenAdmin = response.getBody().getToken();

        System.out.println("Token admin ricevuto: " + tokenAdmin);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenAdmin);
        RegisterRequest registerRequest = new RegisterRequest("admin.test2", "password", "admin@example.com", "Admin", "Test");
        HttpEntity<RegisterRequest> entity = new HttpEntity<>(registerRequest, headers);

        ResponseEntity<String> response2 = restTemplate.postForEntity("http://localhost:" + port + "/register-admin", entity, String.class);
        System.out.println("Risposta registrazione nuovo admin: " + response2.getStatusCode());

        assertThat(response2.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @Order(4)
    @DisplayName("Test di recupero utente corrente")
    public void testGetCurrentUser() {
        System.out.println("Inizio test: recupero utente corrente");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenAdmin);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/current-user", HttpMethod.GET, entity, String.class);
        System.out.println("Risposta recupero utente status: " + response.getStatusCode());
        System.out.println("Contenuto risposta: " + response.getBody());

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("admin.test");
    }
}
