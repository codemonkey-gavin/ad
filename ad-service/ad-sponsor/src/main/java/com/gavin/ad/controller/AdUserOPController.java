package com.gavin.ad.controller;

import com.alibaba.fastjson.JSON;
import com.gavin.ad.exception.AdException;
import com.gavin.ad.service.IAdUserService;
import com.gavin.ad.vo.AdUserRequest;
import com.gavin.ad.vo.AdUserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AdUserOPController {
    private IAdUserService adUserService;

    @Autowired
    public AdUserOPController(IAdUserService adUserService) {
        this.adUserService = adUserService;
    }

    @PostMapping("/create/user")
    public AdUserResponse createUser(@RequestBody AdUserRequest request) throws AdException {
        log.info("ad-sponsor:createUser->{}", JSON.toJSONString(request));
        return adUserService.createAdUser(request);
    }
}
