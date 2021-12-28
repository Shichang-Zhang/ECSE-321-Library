package ca.mcgill.ecse321.library.model;

import javax.persistence.*;

/**
 * user's online account.
 * the online account has username, password and email.
 * the online account will be set to be related with a user.
 */
@Entity
@Table(name = "online_account")
public class OnlineAccount {

    @Column
    private int id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private User user;

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToOne(cascade = {CascadeType.ALL}, optional = true)
    public User getUser() {
        return user;
    }

    public void setUser(User aNewUser) {
        this.user = aNewUser;
    }

}