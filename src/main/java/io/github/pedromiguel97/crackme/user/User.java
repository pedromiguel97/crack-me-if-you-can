package io.github.pedromiguel97.crackme.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 60)
    private String passwordHash;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected  User() {
        // Required by JPA: Hibernate instantiates entities through a no-arg constructor.
    }

    public User(String username, String passwordHash) {
        this.username = Objects.requireNonNull(username, "username must not be null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash must not be null");
        this.createdAt = Instant.now();
    }

    // getters
    public Long getId(){
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Instant getCreatedAt(){
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "'}";
    }
}

