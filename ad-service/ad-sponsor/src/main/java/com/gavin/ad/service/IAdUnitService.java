package com.gavin.ad.service;

import com.gavin.ad.exception.AdException;
import com.gavin.ad.vo.*;

public interface IAdUnitService {
    AdUnitResponse createAdUnit(AdUnitRequest request) throws AdException;

    AdUnitKeywordResponse createAdUnitKeyword(AdUnitKeywordRequest request) throws AdException;

    AdUnitItResponse createAdUnitIt(AdUnitItRequest request) throws AdException;

    AdUnitDistrictResponse createAdUnitDistrict(AdUnitDistrictRequest request) throws AdException;

    AdCreativeUnitResponse createAdCreativeUnit(AdCreativeUnitRequest request)
            throws AdException;
}
