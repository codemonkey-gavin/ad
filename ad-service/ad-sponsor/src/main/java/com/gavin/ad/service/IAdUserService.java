package com.gavin.ad.service;

import com.gavin.ad.exception.AdException;
import com.gavin.ad.vo.AdUserRequest;
import com.gavin.ad.vo.AdUserResponse;

public interface IAdUserService {
    AdUserResponse createAdUser(AdUserRequest request) throws AdException;
}
