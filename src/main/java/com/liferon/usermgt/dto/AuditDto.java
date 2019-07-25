/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.dto;

/**
 *
 * @author olanrewaju.ebenezer
 */
public class AuditDto {
    private String sectionName;
    private String actionName;  
    private String actionType;
    private String details;
    private String sourceDevice;
    private String oldRecord;
    private String newRecord;

    public AuditDto(String sectionName, String actionName, String actionType, String oldRecord, String newRecord) {
        this.sectionName = sectionName;
        this.actionName = actionName; 
        this.actionType = actionType;
        this.oldRecord = oldRecord;
        this.newRecord = newRecord;
    }
    
    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSourceDevice() {
        return sourceDevice;
    }

    public void setSourceDevice(String sourceDevice) {
        this.sourceDevice = sourceDevice;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getOldRecord() {
        return oldRecord;
    }

    public void setOldRecord(String oldRecord) {
        this.oldRecord = oldRecord;
    }

    public String getNewRecord() {
        return newRecord;
    }

    public void setNewRecord(String newRecord) {
        this.newRecord = newRecord;
    }

    @Override
    public String toString() {
        return "AuditDto{" + "sectionName=" + sectionName + ", actionName=" + actionName + ", details=" + details + ", sourceDetails=" + sourceDevice + '}';
    }
              
}
