package com.spring.security.common.web.constant;

public final class ResponseCodeConstants {

    public static final String SUCCESS = "common.success";
    public static final String ERROR = "common.error";
    public static final String SYSTEM_ERROR = "common.system_error";
    public static final String TYPE_ERROR = "common.type_error";

    public static final String HTTP_NOT_FOUND = "http.not_found";
    public static final String HTTP_METHOD_NOT_ALLOWED = "http.method_not_allowed";

    public static final String USER_NOT_FOUND = "user.not_found";
    public static final String USER_DISABLED = "user.disabled";
    public static final String USER_PHONE_NOT_FOUND = "user.phone_not_found";
    public static final String USER_EMAIL_NOT_FOUND = "user.email_not_found";

    public static final String AUTH_LOGIN_FAILED = "auth.login_failed";
    public static final String AUTH_INVALID_CREDENTIALS = "auth.invalid_credentials";
    public static final String AUTH_ACCESS_DENIED = "auth.access_denied";

    public static final String AUTH_PASSWORD_ERROR = "auth.password_error";
    public static final String AUTH_SMS_CODE_ERROR = "auth.sms_code_error";

    public static final String TOKEN_MALFORMED = "token.malformed";
    public static final String TOKEN_EXPIRED = "token.expired";
    public static final String TOKEN_UNSUPPORTED = "token.unsupported";
    public static final String TOKEN_EMPTY = "token.empty";
    public static final String TOKEN_PARSE_ERROR = "token.parse_error";

    public static final String RESOURCE_NOT_FOUND = "resource.not_found";
}