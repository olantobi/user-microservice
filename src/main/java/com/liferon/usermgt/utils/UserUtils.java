package com.liferon.usermgt.utils;

import com.liferon.usermgt.model.User;
import com.liferon.usermgt.dto.UserDto;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class UserUtils {
    
    public static User UserDtoToUser(UserDto userForm) {
        User user = new User();
        user.setUsername(userForm.getUsername());
        user.setEmail(userForm.getEmail());
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setMobileNumber(userForm.getMobileNumber());
        user.setEnabled(userForm.getEnabled());

        return user;
    }
    
    public static List<String> getUserRoles(Principal principal) {
        LinkedHashMap userDetails = (LinkedHashMap) ((OAuth2Authentication) principal).getUserAuthentication().getDetails();        
        List<String> userRoles = ((List<String>) userDetails.get("authorities"));

        return userRoles;
    }
}
