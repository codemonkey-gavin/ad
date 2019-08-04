package com.gavin.ad.service.impl;

import com.gavin.ad.dao.AdCreativeRepository;
import com.gavin.ad.entity.AdCreative;
import com.gavin.ad.exception.AdException;
import com.gavin.ad.service.IAdCreativeService;
import com.gavin.ad.vo.AdCreativeRequest;
import com.gavin.ad.vo.AdCreativeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AdCreativeServiceImpl implements IAdCreativeService {
    private AdCreativeRepository adCreativeRepository;

    @Autowired
    public AdCreativeServiceImpl(AdCreativeRepository adCreativeRepository) {
        this.adCreativeRepository = adCreativeRepository;
    }

    @Override
    public AdCreativeResponse createAdCreative(AdCreativeRequest request) throws AdException {
        AdCreative adCreative = adCreativeRepository.save(request.convertToEntity());
        return new AdCreativeResponse(adCreative.getId(), adCreative.getName());
    }
}
