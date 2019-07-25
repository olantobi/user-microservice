/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.service;

import com.liferon.usermgt.model.AccessLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author olanrewaju.ebenezer
 */
public interface AccessLogService {
    void logAccess(String userId, String loginIP);
    void logAccess(AccessLog accLog);
    AccessLog getLastLogin(String userId);
    Page<AccessLog> getAccessLogs(Pageable pageable);
    Page<AccessLog> getAccessLogs(String userId, Pageable pageable);
}
