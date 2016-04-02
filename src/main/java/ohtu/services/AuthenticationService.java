package ohtu.services;

import ohtu.domain.User;
import ohtu.data_access.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    private UserDao userDao;

    @Autowired
    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean logIn(String username, String password) {
        for (User user : userDao.listAll()) {
            if (canLogIn(user, username, password)) {
                return true;
            }
        }

        return false;
    }
    
    public boolean canLogIn(User user, String username, String password) {
        if (user.getUsername().equals(username)
                    && user.getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean createUser(String username, String password) {
        if (userDao.findByName(username) != null || invalid(username, password)) {
            return false;
        }

        userDao.add(new User(username, password));

        return true;
    }

    private boolean invalid(String username, String password) {
        // validity check of username and password
        if (isUsernameInvalid(username) || isPasswordInvalid(password)) {
            return true;
        }

        return false;
    }
    
    private boolean isUsernameInvalid(String username) {
        if (username.length() < 3) {
            return true;
        }
        
        return false;
    }
    
    private boolean isPasswordInvalid(String password) {
        if (password.length() < 8 || 
            doesPasswordNotHaveNumbersOrSpecialSymbols(password)) {
            return true;
        }
        
        return false;
    }
    
    private boolean doesPasswordNotHaveNumbersOrSpecialSymbols(String password) {
        if (!password.matches(".*\\d+.*") && !password.matches(".*(\\#|\\*).*")) {
            return true;
        }
        
        return false;
    }
}
