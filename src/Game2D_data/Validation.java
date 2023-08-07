package Game2D_data;

import java.util.ArrayList;
import java.util.List;

public class Validation {

    public List<String> validateLogin(String uname, String password) {
        ArrayList<String> err = new ArrayList<String>();

        if (uname.isEmpty()) {
            err.add("User Name can not be empty");
        } else if (uname.length() < 4) {
            err.add("User Name is too short");
        } else if (uname.length() > 20) {
            err.add("User Name is too long");
        }

        if (password.isEmpty()) {
            err.add("Password can not be empty");
        } else if (password.length() < 4) {
            err.add("Password is too short");
        } else if (password.length() > 20) {
            err.add("Password is too long");
        }
        return err;
    }
}
