package com.liferon.usermgt.component;

import com.liferon.usermgt.model.Client;
import com.liferon.usermgt.service.ClientService;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.liferon.usermgt.model.AccessLog;
import com.liferon.usermgt.service.AccessLogService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Component;

@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthFilter implements Filter {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccessLogService accessLogService;
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) {
        
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        /*
        Map<String, String[]> paramMap = request.getParameterMap();
        System.out.println("Url: "+request.getRequestURI());
        System.out.println("IP Address: "+request.getRemoteAddr());
        
        for (String key : paramMap.keySet()) {           
            System.out.println(key+ " = "+ Arrays.toString(paramMap.get(key)));       
        }        
        *
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "authorization, content-type");
        response.addHeader("Access-Control-Expose-Headers", "x-requested-with");
        */
        MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
        
        if (request.getMethod().equalsIgnoreCase("POST") && request.getRequestURI().equals("/oauth/token")) {
            
            if (request.getHeader("Authorization") == null) {
                String clientId = request.getParameter("client_id");
                if (clientId != null && !clientId.isEmpty()) {
                    Client client = clientService.getByClientId(clientId);

                    if (client != null && client.getIpAddress().equals(request.getRemoteAddr())) {                   

                        String rawHeaderString = client.getClientId()+":"+client.getClientSecret();
                        String authHeader = Base64.encodeBase64String(rawHeaderString.getBytes());

                        mutableRequest.putHeader("Authorization", "Basic "+authHeader);
                    }
                }            
            }
            
            //System.out.println("Auth Response: "+response.getStatus());
            // LOG ALL ACCESS
            String username = request.getParameter("username");
            if (username != null && !username.isEmpty() && response.getStatus() == HttpStatus.OK.value()) {
                AccessLog accessLog = new AccessLog(username, request.getRemoteAddr());
                //System.out.println("Access Log: "+accessLog + " :: Response: "+response.getStatus());
                accessLogService.logAccess(accessLog);
            }
        }
        
      /*  if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            //response.setStatus(HttpServletResponse.SC_OK);
        } else {
*/
            try {
                filterChain.doFilter(mutableRequest, response);
            } catch (IOException | ServletException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
     //   }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }    
}
