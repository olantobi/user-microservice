/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.TIMESTAMP;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * @author olanrewaju.ebenezer
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> {
    
    @CreatedBy
    @Column(name="created_by")
    protected U createdBy;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSZ")
    @CreatedDate
    @Temporal(TIMESTAMP)
    @Column(name="created_date")
    protected Date createdDate;
    
    @LastModifiedBy
    @Column(name="last_modified_by")
    protected U lastModifiedBy;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSZ")
    @LastModifiedDate
    @Temporal(TIMESTAMP)
    @Column(name="last_modified_date")
    protected Date lastModifiedDate;
}
