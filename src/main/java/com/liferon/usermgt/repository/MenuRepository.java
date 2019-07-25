/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import com.liferon.usermgt.model.Menu;
import com.liferon.usermgt.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author olanrewaju.ebenezer
 */
public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> getByMenuName(String menuName);
    List<Menu> findByPermissionIn(Collection<Permission> permissions);
    List<Menu> findByEnabledTrue();
    List<Menu> findByMenuUrlIsNull();    
}
