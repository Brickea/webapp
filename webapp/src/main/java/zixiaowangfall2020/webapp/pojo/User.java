package zixiaowangfall2020.webapp.pojo;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.util.Date;

public class User {
    @Null(message = "id should not be define by request!")
    Integer id;
    @NotNull(message = "email should not be blank")
    @Pattern(regexp = "^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})$", message = "email format is invalid!")
    String userName;
    //    Check for password length of 8 or shorter which should be rejected.
    //    Check for simple all char passwords.
    //    Check for complex passwords.
    @NotNull(message = "password should not be blank")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$", message = "password's length should longer than 8 and contain at least one char and one number!")
    String password;
    @NotNull(message = "firstName should not be blank")
    String firstName;
    @NotNull(message = "lastName should not be blank")
    String lastName;
    @Null(message = "accountCreated should not be define by request!")
    String accountCreated;
    @Null(message = "accountUpdated should not be define by request!")
    String accountUpdated;

    public User() {

    }

    public User(Integer id, String userName, String password, String firstName, String lastName, String accountCreated, String accountUpdated) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountCreated = accountCreated;
        this.accountUpdated = accountUpdated;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAccountCreated() {
        return accountCreated;
    }

    public void setAccountCreated(String accountCreated) {
        this.accountCreated = accountCreated;
    }

    public String getAccountUpdated() {
        return accountUpdated;
    }

    public void setAccountUpdated(String accountUpdated) {
        this.accountUpdated = accountUpdated;
    }
}
