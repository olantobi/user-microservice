/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Ebenezer
 */
@Entity
@Table(name="access_log")
//@SQLInsert(sql = "INSERT INTO access_log (login_ip, login_time, user_id) VALUES(?,NOW(),?)")
public class AccessLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;          
    @Column(name="user_id")
    private String userId;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSZ")
    @Column(name="login_time")
    private Date loginTime;
    @Column(name="login_ip")
    private String loginIP;

    public AccessLog() {
    }
    
    public AccessLog(String userId, String loginIP) {       
        this.userId = userId;
        this.loginIP = loginIP; 
        loginTime = new Date();
    }    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AccessLog{" + "logId=" + id + ", user=" + userId + ", loginTime=" + loginTime + ", loginIP=" + loginIP + '}';
    }        
}
