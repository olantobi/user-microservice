/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.repository;

import java.util.List;
import com.liferon.usermgt.model.AccessLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author olanrewaju.ebenezer
 */
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    Page<AccessLog> findByuserId(String userId, Pageable pageable);
    List<AccessLog> findByuserId(String userId);
}
