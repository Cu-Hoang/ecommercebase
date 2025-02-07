package com.project.ecommercebase.constant;

public class Endpoint {
    public static final String ENDPOINT_USER = "/users";
    public static final String ENDPOINT_AUTH = "/auths";
    public static final String ENDPOINT_SHOP = "/shops";
    public static final String ENDPOINT_CATEGORY = "/categories";

    public static class UserEndpoint {
        public static final String REGISTER = "/register";
        public static final String VERIFY_EMAIL_CODE = "/register/verify-email-code";
        public static final String REGISTER_CUSTOMER = "/register/customer";
        public static final String REGISTER_VENDOR = "/register/vendor";
        public static final String UPDATE_TO_VENDOR = "/update-to-vendor";
        public static final String GET_BY_ID = "/{id}";
        public static final String UPDATE_USER = "/update";
        public static final String UPDATE_USER_PASSWORD = "/update-password";
        public static final String CREATE_SHOP = "/create-shop";
    }

    public static class AuthEndpoint {
        public static final String LOGIN_PASSWORD = "/login-password";
        public static final String GENERATE_NEW_TOKEN = "/generate-new-token";
        public static final String LOGOUT = "/logout";
        public static final String LOGOUT_ALL_DEVICES = "/logout-all-devices";
        public static final String EMAIL_OTP_PASSWORD = "/email-otp-password";
        public static final String LOGIN_OTP = "/login-otp";
        public static final String EMAIL_OTP_RESET_PASSWORD = "/email-otp-reset-password";
        public static final String RESET_PASSWORD = "/reset-password";
    }

    public static class ShopEndpoint {}

    public static class CategoryEndpoint {
        public static final String CREATE_CATEGORY = "/create-category";
        public static final String GET_ALL_SUBCATEGORY_BY_ID = "/get-all-subcategory/{id}";
        public static final String GET_ALL_SUBCATEGORY_BY_ID_AND_SHOPID = "/get-all-subcategory/{shopId}/{id}";
        public static final String GET_BY_ID = "/{id}";
        public static final String GET_BY_SHOPID = "/shop/{shopId}";
        public static final String UPDATE_BY_ID = "/update/{id}";
    }
}
