package com.authentication.auth.authentication.handler.auth.openApi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OpenApiLoginInfo {

    private String appId;
    private String appSecret;
    private String merchantName; // 下游商户名称
}
