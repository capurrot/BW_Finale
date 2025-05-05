package it.epicode.bw.finale.clienti;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}