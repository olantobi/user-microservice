/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author olanrewaju.ebenezer
 */
public class ClientDto {
    @NotEmpty(message="Client Id is a required field")
    private String clientId;
    @NotEmpty(message="Client Secret is a required field")
    private String clientSecret;
    @NotEmpty(message="IP Address is a required field")
    private String ipAddress;
    private boolean enabled=true;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "ClientDto{" + "clientId=" + clientId + ", clientSecret=" + clientSecret + ", ipAddress=" + ipAddress + '}';
    }        
}
