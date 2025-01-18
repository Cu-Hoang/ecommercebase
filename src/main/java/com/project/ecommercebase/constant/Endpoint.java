package com.project.ecommercebase.constant;

public class Endpoint {
    public static final String ENDPOINT_USER = "/users";
    public static final String ENDPOINT_AUTH = "/auths";

    public static class UserEndpoint {
        public static final String CREATE_USER = "/register/user";
        public static final String VERIFY_EMAIL_CODE = "/register/verify-email-code";
        public static final String REGISTER_CUSTOMER = "/register/customer";
        public static final String REGISTER_VENDOR = "/register/vendor";
        public static final String UPDATE_TO_VENDOR = "/update-to-vendor";
        public static final String GET_BY_ID = "/{id}";
        public static final String UPDATE_USER = "/update/{id}";
    }

    public static class AuthEndpoint {
        public static final String LOGIN = "/login";
        public static final String GENERATE_NEW_TOKEN = "/generate-new-token";
    }
}
