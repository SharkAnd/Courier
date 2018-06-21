package dell.courier.client.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Client {

    public String userID;
    public String firstName ;
    public String lastName ;
    public String phone ;
    public String email ;

    public Client() {
    }

    public Client(String userID, String firstName, String lastName, String phone, String email) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }
}
