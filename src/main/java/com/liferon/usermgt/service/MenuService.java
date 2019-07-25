/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.service;

import java.util.Collection;
import java.util.List;
import com.liferon.usermgt.model.Menu;
import com.liferon.usermgt.model.Permission;

/**
 *
 * @author olanrewaju.ebenezer
 */
public interface MenuService {
    Menu getMenu(Long menuId);    
    Menu getMenuByName(String menuName);
    Menu saveMenu(Menu menu);
    Menu updateMenu(Menu menu);
    void deleteMenu(Menu menu);
    void deleteMenu(Long menuId);
    List<Menu> getAll();
    List<Menu> getAllEnabled();
    List<Menu> getParentMenus();
    List<Menu> getChildrenMenus(Menu parentMenu);
    boolean menuExists(Menu menu);
    boolean menuNameExists(String menuName);
    List<Menu> getPermittedMenus(Collection<Permission> permissions);
}
