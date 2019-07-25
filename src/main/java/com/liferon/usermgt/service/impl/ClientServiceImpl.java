/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.service.impl;

import com.liferon.usermgt.repository.ClientRepository;
import com.liferon.usermgt.model.Client;
import com.liferon.usermgt.service.ClientService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author olanrewaju.ebenezer
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;
    
    @Override
    public Client getClient(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public Client getByClientId(String clientId) {
        return clientRepository.getByClientId(clientId);
    }

    @Override
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public List<Client> getAll(boolean getAll) {
        return clientRepository.findAll();
    }
    
}
