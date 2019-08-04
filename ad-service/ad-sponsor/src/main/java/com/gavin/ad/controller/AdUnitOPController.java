package com.gavin.ad.controller;

import com.alibaba.fastjson.JSON;
import com.gavin.ad.exception.AdException;
import com.gavin.ad.service.IAdUnitService;
import com.gavin.ad.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AdUnitOPController {
    private IAdUnitService adUnitService;

    @Autowired
    public AdUnitOPController(IAdUnitService adUnitService) {
        this.adUnitService = adUnitService;
    }

    @PostMapping("/create/unit")
    public AdUnitResponse createUnit(@RequestBody AdUnitRequest request) throws AdException {
        log.info("ad-sponsor: createAdUnit -> {}", JSON.toJSONString(request));
        return adUnitService.createAdUnit(request);
    }

    @PostMapping("/create/unitkeyword")
    public AdUnitKeywordResponse createUnitKeyword(@RequestBody AdUnitKeywordRequest request) throws AdException {
        log.info("ad-sponsor: createAdUnitKeyword -> {}", JSON.toJSONString(request));
        return adUnitService.createAdUnitKeyword(request);
    }

    @PostMapping("/create/unitit")
    public AdUnitItResponse createUnitIt(@RequestBody AdUnitItRequest request) throws AdException {
        log.info("ad-sponsor: createAdUnitIt -> {}", JSON.toJSONString(request));
        return adUnitService.createAdUnitIt(request);
    }

    @PostMapping("/create/unitDistrict")
    public AdUnitDistrictResponse createUnitDistrict(@RequestBody AdUnitDistrictRequest request) throws AdException {
        log.info("ad-sponsor: createAdUnitDistrict -> {}", JSON.toJSONString(request));
        return adUnitService.createAdUnitDistrict(request);
    }

    @PostMapping("/create/creativeUnit")
    public AdCreativeUnitResponse createCreativeUnit(@RequestBody AdCreativeUnitRequest request) throws AdException {
        log.info("ad-sponsor: createCreativeUnit -> {}", JSON.toJSONString(request));
        return adUnitService.createAdCreativeUnit(request);
    }
}
