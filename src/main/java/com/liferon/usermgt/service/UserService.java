/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.service;

import com.liferon.usermgt.model.User;
import java.util.List;

/**
 *
 * @author olanrewaju.ebenezer
 */
public interface UserService {
    public User findByUsername(String username);
    public User findOne(String userId);
    public User findById(String userid);
    public User findByEmail(String email);

    public List<User> getAll();

    public long count();
    
    public User save(User user);
    public User update(User user);
    public boolean isUserExist(User user);
    public User createUser(User user, String requestIp);
    /*
    public User editUser(UserDto userForm);

    public User editUser(String username, UserDto editedUser);

    public User saveUser(UserDto userForm);
    
    public User saveUser(UserDto userForm, String requestIp);
    */
    public void deleteUser(User user);

    public List<User> searchUsers(String keyword);
}
