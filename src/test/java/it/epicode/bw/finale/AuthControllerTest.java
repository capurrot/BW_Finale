package it.epicode.bw.finale;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.epicode.bw.finale.auth.*;
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

    @Test
    @Order(2)
    @DisplayName("Test di registrazione utente")
    public void testRegisterUser() {
        RegisterRequest registerRequest = new RegisterRequest("giorgio.mastrota", "password", "mondialcasa@example.com", "Giorgio", "Mastrota");
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:"+ port +"/register", registerRequest, String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
    @Test
    @Order(1)
    @DisplayName("Test di login utente")
    public void testLoginUser() {
        AppUser appUser = new AppUser();
        appUser.setUsername("gianni.bugno");
        appUser.setPassword(passwordEncoder.encode("password"));
        appUser.setRoles(Set.of(Role.ROLE_USER));
        appUserRepository.save(appUser);
        LoginRequest loginRequest = new LoginRequest("gianni.bugno", "password");
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:"+ port +"/login", loginRequest, String.class);
        tokenNormale = response.getBody();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
    @Test
    @Order(3)
    @DisplayName("Test di registrazione admin")
    public void testRegisterAdmin() {
        //creo un admin
        AppUser appUser = new AppUser();
        appUser.setUsername("admin.test");
        appUser.setPassword(passwordEncoder.encode("adminpwd"));
        appUser.setRoles(Set.of(Role.ROLE_ADMIN));
        appUserRepository.save(appUser);

        //accedo con l'admin appena creato
        LoginRequest loginRequest = new LoginRequest("admin.test", "adminpwd");
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity("http://localhost:"+ port +"/login", loginRequest, AuthResponse.class);
        tokenAdmin = response.getBody().getToken();
        //creo un admin dal controller
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenAdmin);
        RegisterRequest registerRequest = new RegisterRequest("admin.test2", "password", "admin@example.com", "Admin", "Test");
        HttpEntity<RegisterRequest> entity = new HttpEntity<>(registerRequest, headers);
        ResponseEntity<String> response2 = restTemplate.postForEntity("http://localhost:"+ port +"/register-admin", entity, String.class);
        assertThat(response2.getStatusCodeValue()).isEqualTo(200);
    }
    @Test
    @Order(4)
    @DisplayName("Test di recupero utente corrente")
    public void testGetCurrentUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(tokenAdmin);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange("http://localhost:"+ port +"/current-user", HttpMethod.GET , entity ,String.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).contains("admin.test");
    }





}