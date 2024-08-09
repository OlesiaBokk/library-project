package ru.itgirl.library_project.model.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class MyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "users_role", joinColumns = @JoinColumn(name = "user_id"),
//    inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JoinTable(
            name = "users_role",
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", table = "users"),
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", table = "role"))
    private Set<Role> roles = new HashSet<>();

    public MyUser(String login) {
        this.login = login;
    }

    public MyUser(){
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
