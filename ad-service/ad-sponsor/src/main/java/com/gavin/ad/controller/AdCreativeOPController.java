package com.gavin.ad.controller;

import com.alibaba.fastjson.JSON;
import com.gavin.ad.exception.AdException;
import com.gavin.ad.service.IAdCreativeService;
import com.gavin.ad.vo.AdCreativeRequest;
import com.gavin.ad.vo.AdCreativeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AdCreativeOPController {
    private IAdCreativeService adCreativeService;

    public AdCreativeOPController(IAdCreativeService adCreativeService) {
        this.adCreativeService = adCreativeService;
    }

    @PostMapping("/create/creative")
    public AdCreativeResponse createCreative(@RequestBody AdCreativeRequest request) throws AdException {
        log.info("ad-sponsor: createCreative -> {}", JSON.toJSONString(request));
        return adCreativeService.createAdCreative(request);
    }
}
