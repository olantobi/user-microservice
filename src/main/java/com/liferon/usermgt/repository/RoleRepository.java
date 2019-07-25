/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.repository;

import com.liferon.usermgt.model.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Ebenezer
 */
public interface RoleRepository extends JpaRepository<Role, String> {

    List<Role> findByEnabledTrue();
    Optional<Role> getByRoleName(String roleName);
        
    @Transactional
    @Modifying
    @Query(value="UPDATE role SET role_name = substring(role_name, 6, LENGTH(role_name)) WHERE substring(role_name, 1, 5) = 'ROLE_'", nativeQuery = true)
    int updateRoleNames();

}
