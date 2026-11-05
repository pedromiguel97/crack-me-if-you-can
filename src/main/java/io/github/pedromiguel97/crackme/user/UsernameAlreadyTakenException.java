package io.github.pedromiguel97.crackme.user;

public class UsernameAlreadyTakenException extends RuntimeException {

    public UsernameAlreadyTakenException(String username) {
        super("Username already taken: " + username);
    }
}