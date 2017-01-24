package com.show.blue;

/**
 * Created by apple on 2016/10/26.
 */

public class LoginPresenter {

    private UserManager mUserManager = new UserManager();
    private PasswordValidator passwordValidator = new PasswordValidator();

    public void login(String username, String password) {
        if (username == null || username.length() == 0) return;
        if (password == null || password.length() < 6) return;
        if (passwordValidator.verifyPassword(password)) {
            return;
        }
        mUserManager.performLogin(username, password);
    }

    public void setUserManager(UserManager userManager) {  //<==
        this.mUserManager = userManager;
    }

    public void setPasswordValidator(PasswordValidator passwordValidator) {
        this.passwordValidator = passwordValidator;
    }
}
