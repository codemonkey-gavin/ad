package com.gavin.ad.service;

import com.gavin.ad.exception.AdException;
import com.gavin.ad.vo.AdCreativeRequest;
import com.gavin.ad.vo.AdCreativeResponse;

public interface IAdCreativeService {
    AdCreativeResponse createAdCreative(AdCreativeRequest request) throws AdException;
}
