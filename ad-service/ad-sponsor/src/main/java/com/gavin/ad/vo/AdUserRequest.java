package com.gavin.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUserRequest {
    private String userName;

    public boolean validate() {
        return !StringUtils.isEmpty(userName);
    }
}
