/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.service.impl;

import java.util.Collection;
import java.util.List;

import com.liferon.usermgt.repository.MenuRepository;
import com.liferon.usermgt.model.Menu;
import com.liferon.usermgt.model.Permission;
import com.liferon.usermgt.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author olanrewaju.ebenezer
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public Menu getMenuByName(String menu) {
        return menuRepository.getByMenuName(menu).get();
    }

    @Override
    @Transactional
    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public Menu getMenu(Long menuId) {
        return menuRepository.findById(menuId).orElse(null);
    }

    @Override
    public Menu updateMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    public void deleteMenu(Menu menu) {
        menuRepository.delete(menu);
    }

    @Override
    public void deleteMenu(Long menuId) {
        menuRepository.deleteById(menuId);
    }

    @Override
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    public List<Menu> getAllEnabled() {
        return menuRepository.findByEnabledTrue();
    }

    @Override
    public boolean menuExists(Menu menu) {
        return menuNameExists(menu.getMenuName());
    }

    @Override
    public boolean menuNameExists(String menuName) {
        return menuRepository.getByMenuName(menuName).isPresent();
    }

    @Override
    public List<Menu> getPermittedMenus(Collection<Permission> permissions) {
        return menuRepository.findByPermissionIn(permissions);
    }

    @Override
    public List<Menu> getParentMenus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Menu> getChildrenMenus(Menu parentMenu) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
