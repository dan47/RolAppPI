package authorization.register;

public class RegisterCredentials {

    private String username;

    private String password;

    private String confirmPassword;

    public RegisterCredentials(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() { return confirmPassword; }

}
