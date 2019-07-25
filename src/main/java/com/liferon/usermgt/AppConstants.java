package com.liferon.usermgt;

public class AppConstants {

    public static class MobileClient {

        public static final String CLIENT_ID = "android-client";
        public static final String CLIENT_SECRET = "9CB8662B93A95059DEBB66A64D113677";        
        public static final String[] AUTH_SCOPES = {"read", "write", "admin"};
        public static final String[] GRANT_TYPES = {"password", "authorization_code", "refresh_token"};
        public static final String[] AUTHORITIES = {"ROLE_SYSADMIN"};
    }

    public static class FrontEndClient {

        public static final String CLIENT_ID = "angular-client";
        public static final String CLIENT_SECRET = "9CB8662B93A95059DEBB66A64D113677";
        public static final String[] AUTH_SCOPES = {"read", "write", "admin"};
        public static final String[] GRANT_TYPES = {"password", "authorization_code", "refresh_token"};
        //public static final String[] AUTHORITIES = {"ROLE_SYSADMIN"};
    }

    public static class BackEndClient {

        public static final String CLIENT_ID = "backend-client";
        public static final String CLIENT_SECRET = "9CB8662B93A95059DEBB66A64D113677";
        public static final String[] AUTH_SCOPES = {"read", "write", "admin"};
        public static final String[] GRANT_TYPES = {"password", "authorization_code", "refresh_token"};
        public static final String[] AUTHORITIES = {"ROLE_ADMIN"};
    }

    public static class Pages {

        public static final String ADMIN_LANDING_PAGE = "/admin/dashboard";
        public static final String DEVELOPER_LANDING_PAGE = "/swagger-ui.html";
    }

    public static final int ACCESS_TOKEN_VALIDITY = 108000;
    public static final int REFRESH_TOKEN_VALIDITY = 2592000;
    public static final String SIGNING_KEY = "9CB8662B93A95059DEBB66A64D113677";
}
