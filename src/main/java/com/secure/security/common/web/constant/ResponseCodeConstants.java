package com.secure.security.common.web.constant;

public final class ResponseCodeConstants {

    // 通用状态码
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String SYSTEM_ERROR = "system.error";
    public static final String API_NOT_FOUND = "api.not.found";

    // 用户模块
    public static final String USER_NOT_FOUND = "user.not_found";
    public static final String USER_DISABLED = "user.disabled";

    // 登录模块
    public static final String LOGIN_SUCCESS = "login.success";
    public static final String LOGIN_FAIL = "login.fail";
    public static final String INVALID_CREDENTIALS = "auth.invalid_credentials";

    // 资源模块
    public static final String RESOURCE_NOT_FOUND = "resource.not_found";
}
