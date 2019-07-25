/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.repository;

import com.liferon.usermgt.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author olanrewaju.ebenezer
 */
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client getByClientId(String clientId);
}
