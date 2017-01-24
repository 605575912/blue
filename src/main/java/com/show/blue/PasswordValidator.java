package com.show.blue;

/**
 * Created by apple on 2016/10/26.
 */

public class PasswordValidator {
    public boolean verifyPassword(String password) {
        System.out.print("verifyPassword" + password+"\n");
        if ("123456".equals(password)) {
            return true;
        }
        return false;
    }
}
