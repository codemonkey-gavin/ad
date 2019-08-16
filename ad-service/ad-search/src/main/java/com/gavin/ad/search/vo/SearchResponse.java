package com.gavin.ad.search.vo;

import com.gavin.ad.index.creative.AdCreativeObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private Map<String, List<Creative>> adSlotsAds = new HashMap<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Creative {
        private Long adId;
        private String adUrl;
        private Integer width;
        private Integer height;
        private Integer type;
        private Integer materialType;
        // 展示监测
        private List<String> showMonitorUrl;
        // 点击监测
        private List<String> clickMonitorUrl;
    }

    public static Creative convent(AdCreativeObject creativeObject) {
        Creative creative = new Creative();
        creative.setAdId(creativeObject.getId());
        creative.setAdUrl(creativeObject.getUrl());
        creative.setWidth(creativeObject.getWidth());
        creative.setHeight(creativeObject.getHeight());
        creative.setType(creativeObject.getType());
        creative.setMaterialType(creativeObject.getMaterialType());
        return creative;
    }
}
