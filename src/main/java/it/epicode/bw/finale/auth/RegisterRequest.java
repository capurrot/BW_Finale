package it.epicode.bw.finale.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String nome;
    private String cognome;
}
