package com.qcl.push;

import lombok.Data;

/**
 * 微信返回的Access_Token对应的bean
 */
@Data
public class AccessToken {
    private String access_token;
    private String expires_in;
}
