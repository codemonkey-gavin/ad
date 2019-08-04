package com.gavin.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanRequest {
    private Long id;
    private Long userId;
    private String planName;
    private String startDate;
    private String endDate;

    public boolean createValidate() {
        return null != userId && !StringUtils.isEmpty(planName) && !StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate);
    }

    public boolean updateValidate() {
        return null != id && null != userId;
    }

    public boolean deleteValidate() {
        return null != id && null != userId;
    }
}
