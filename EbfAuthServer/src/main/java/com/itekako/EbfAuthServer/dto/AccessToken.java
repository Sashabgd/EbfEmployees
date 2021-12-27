package com.itekako.EbfAuthServer.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AccessToken {
    private String accessToken;
}
