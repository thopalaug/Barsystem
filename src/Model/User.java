package Model;

public class User {
    private String id;
    private String password;
    private int admin;


    public User(String id, String password, int admin){
        this.id = id;
        this.password = password;
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public int getAdmin() {
        return admin;
    }
}
