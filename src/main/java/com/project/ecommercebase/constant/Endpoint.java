package com.project.ecommercebase.constant;

public class Endpoint {
    public static final String ENDPOINT_USER = "/users";

    public static class UserEndpoint {
        public static final String CREATE_USER = "/register/user";
        public static final String CHECK_CODE = "/register/code";
        public static final String REGISTER_CUSTOMER = "/register/customer";
        public static final String REGISTER_VENDOR = "/register/vendor";
    }
}
