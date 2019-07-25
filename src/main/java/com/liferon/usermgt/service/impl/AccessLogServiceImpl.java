/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.service.impl;

import com.liferon.usermgt.repository.AccessLogRepository;
import com.liferon.usermgt.model.AccessLog;
import com.liferon.usermgt.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 *
 * @author olanrewaju.ebenezer
 */
@Service
public class AccessLogServiceImpl implements AccessLogService {

    @Autowired
    private AccessLogRepository accessLogRepository;
    
    @Override
    public void logAccess(String userId, String loginIP) {
        accessLogRepository.save(new AccessLog(userId, loginIP));
    }

    @Override
    public void logAccess(AccessLog accessLog) {
        accessLogRepository.save(accessLog);
    }

    @Override
    public AccessLog getLastLogin(String userId) {
        Pageable pageable = new PageRequest(0, 1, Direction.DESC, "id");
        return accessLogRepository.findByuserId(userId, pageable).getContent().get(0);
    }

    @Override
    public Page<AccessLog> getAccessLogs(Pageable pageable) {
        return accessLogRepository.findAll(pageable);
    }

    @Override
    public Page<AccessLog> getAccessLogs(String userId, Pageable pageable) {
        return accessLogRepository.findByuserId(userId, pageable);
    }
    
}
