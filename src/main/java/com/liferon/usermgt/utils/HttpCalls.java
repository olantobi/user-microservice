/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liferon.usermgt.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Ebenezer
 */
public class HttpCalls {
    private String url;
    private String urlParameters;

    public HttpCalls() {
    }

    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlParameters() {
        return urlParameters;
    }

    public void setUrlParameters(String urlParameters) {
        this.urlParameters = urlParameters;
    }    
    
    public HttpCalls(String url, String urlParameters) {
        this.url = url;
        this.urlParameters = urlParameters;
    }
    
    public String sendPost() {
        String responseString = "";
        try {            
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");                       
            
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);
            
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            responseString = response.toString();
            //print result
            System.out.println(response.toString());
        } catch (MalformedURLException ex) {
            Logger.getLogger(HttpCalls.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(HttpCalls.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HttpCalls.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return responseString;
    }
}
