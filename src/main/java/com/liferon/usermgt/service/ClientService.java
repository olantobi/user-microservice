/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.service;

import com.liferon.usermgt.model.Client;
import java.util.List;

/**
 *
 * @author olanrewaju.ebenezer
 */
public interface ClientService {
    Client getClient(Long id);
    Client getByClientId(String clientId);
    Client saveClient(Client client);
    Client updateClient(Client client);        
    List<Client> getAll(boolean getAll);
}
