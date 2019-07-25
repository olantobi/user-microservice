/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.service.impl;

import com.liferon.usermgt.repository.UserRepository;
import com.liferon.usermgt.model.User;
import com.liferon.usermgt.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author olanrewaju.ebenezer
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

     @Override
    public User findOne(String userId) {
        return this.userRepository.findById(userId).orElse(null);
    }

     @Override
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    /*
  public Iterable<User> findAll() {
    return this.userRepository.findAll();
  }
     */
     @Override
    public List<User> getAll() {
        return this.userRepository.findAll();
    }

     @Override
    public long count() {
        return this.userRepository.count();
    }

     @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }
    /*
     @Override
    public User editUser(UserDto userForm) {
        User user = userRepository.findOne(userForm.getUserId());
        user.setRole(new Role(userForm.getRole()));
        user.setEmail(userForm.getEmail());
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setMobileNumber(userForm.getMobileNumber());
        user.setEnabled(userForm.getStatus() == 1 ? true : false);

        return userRepository.save(user);
    }

     @Override
    public User editUser(String username, UserDto editedUser) {
        User user = userRepository.findByEmail(username);

        if (editedUser.getRole() != null) {
            user.setRole(new Role(editedUser.getRole()));
        }
        if (editedUser.getFirstName() != null) {
            user.setFirstName(editedUser.getFirstName());
        }
        if (editedUser.getLastName() != null) {
            user.setLastName(editedUser.getLastName());
        }
        if (editedUser.getMobileNumber() != null) {
            user.setMobileNumber(editedUser.getMobileNumber());
        }
        if (editedUser.getStatus() != 0) {
            user.setEnabled(editedUser.getStatus() == 1 ? true: false);
        }

        return userRepository.save(user);
    }
*
     @Override
    public User saveUser(UserDto userForm) {
        User user = UserUtils.UserDtoToUser(userForm);                  
        Role role = roleRepository.getByRoleName(userForm.getRole());

        System.out.println("Role: "+role);
        user.setRole(role);

        return userRepository.save(user);
    }
    
     @Override
    public User saveUser(UserDto userForm, String requestIp) {
        User user = UserUtils.UserDtoToUser(userForm);        
        user.setCreatedIp(requestIp);
        
        Role role = roleRepository.getByRoleName(userForm.getRole());
        
        user.setRole(role);

        return userRepository.save(user);
    }
    */
    @Override
    public User createUser(User user, String requestIp) {                
        user.setCreatedIp(requestIp);                

        return userRepository.save(user);
    }
 
     @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

     @Override
    public List<User> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword);
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public boolean isUserExist(User user) {
        return (findByUsername(user.getUsername()) != null || findByEmail(user.getEmail()) != null);
    }
}
