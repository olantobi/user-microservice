/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.utils;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Ebenezer
 */
public class Utils {
    
    private static final DecimalFormat dFormat = new DecimalFormat("#,###.00");
    public static String leftSpacePad(String data, int desiredLength) {
        
        int dataLen = data.length();
        if (dataLen < desiredLength) {
            int remLen = desiredLength - dataLen;            
            data = StringUtils.repeat("&nbsp;", remLen)+data;            
        }
        return data;
    }
    
    public static String leftZeroPad(String data, int desiredLength) {
        
        int dataLen = data.length();
        if (dataLen < desiredLength) {
            int remLen = desiredLength - dataLen;            
            data = StringUtils.repeat('0', remLen)+data;            
        }
        return data;
    }
    
    public static String formatAmount(double amount) {
        return dFormat.format(amount);
    }
    
    public static String generateHashedStringSHA512(String echoString) throws Exception {         
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.update(echoString.getBytes());
        byte[] echoData = messageDigest.digest();
        String out = "";
        StringBuilder sb = new StringBuilder();
        for (byte element : echoData) {
            sb.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
        }
        out = sb.toString();
        return out;
    } 
    
    public static String arrayFind(String[] hayStack, String needle) {
        for (String hay : hayStack) {
            if (hay.contains(needle))
                return hay;
        }
        return null;
    }
    
    public static String fromJsonpToJson(String jsonpResponse) {
        int index1 = jsonpResponse.indexOf("jsonp({") + 6;
        int index2 = jsonpResponse.indexOf("})") + 1;
        String jsonOutput = jsonpResponse.substring(index1, index2);
                        
        return jsonOutput;
    }
    
    public static Date parseStringToDate(String dateString) throws ParseException {
        return parseStringToDate("yyyy-mm-dd", dateString);
    }
    
    public static Date parseStringToDate(String dateFormat, String dateString) throws ParseException {
        try {
            //private final NumberFormat currencyFormatter = NumberFormat.getNumberInstance();            
            
            return new SimpleDateFormat(dateFormat).parse(dateString);
        } catch (ParseException ex) {
            System.err.println("ParseException: "+ex.getMessage());
            throw ex;
            //return null;
        }
                
    }
    
    public static String formatDateToString(String dateFormat, Date dateValue) {
        
        return new SimpleDateFormat(dateFormat).format(dateValue);                        
    }
    
    public static double parseStringtoDouble(String amountString) throws ParseException {
        try {                             
            return NumberFormat.getNumberInstance().parse(amountString).doubleValue();
        } catch (ParseException ex) {
            System.err.println("ParseException: "+ex.getMessage());
            throw ex;
            //return 0.00;
        }                
    }
    
    public static String generateOrderId(int sequenceNo) {
        
        String orderId = formatDateToString("yyMMdd", new Date()) + leftZeroPad(String.valueOf(sequenceNo), 6);
        //yyMMdd
       // String orderId = sdf.format(new Date()) + Utils.leftZeroPad(String.valueOf(seqGen.getSequenceNumber("order_id")), 6);
        return orderId;
    }
         
}
