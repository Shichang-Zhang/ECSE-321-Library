package ca.mcgill.ecse321.library.dto;

/**
 * data transfer object of online account.
 * have id, username,email fields.
 * have getter and setter methods for private fields.
 */
public class OnlineAccountDto {

    private int id;
    private String username;
    private String email;
    private int userId;

    public int getId() {
        return id;
    }

    public OnlineAccountDto(int id) {
        this.id = id;
    }

    public OnlineAccountDto(int id, String username, String email, int userId) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userId=userId;
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


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
