/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.liferon.usermgt.config.Auditable;

/**
 *
 * @author olanrewaju.ebenezer
 */
@Entity
public class Menu extends Auditable<String> {
    @Id    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  
    @Column(name="menu_name")
    private String menuName;
    @Column(name="menu_url")
    private String menuUrl;
    private boolean enabled;
    @Column(name="menu_level")
    private int menuLevel;
    @ManyToOne
    //@JoinColumn(name = "permission+")
    private Permission permission;
    @OneToMany
    @JoinColumn(name="parent_id")    
    private Set<Menu> childrenMenu;    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public int getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(int menuLevel) {
        this.menuLevel = menuLevel;
    }

    public Set<Menu> getChildrenMenu() {
        return childrenMenu;
    }

    public void setChildrenMenu(Set<Menu> childrenMenu) {
        this.childrenMenu = childrenMenu;
    }
    
    public void addChildMenu(Menu childMenu) {
        
        if (childrenMenu == null)
            childrenMenu = new HashSet<>();
        
        this.childrenMenu.add(childMenu);
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Menu{" + "id=" + id + ", menuName=" + menuName + ", menuUrl=" + menuUrl + ", enabled=" + enabled + ", menuLevel=" + menuLevel + ", permission=" + permission.getPermissionKey() + '}';
    }          
}
