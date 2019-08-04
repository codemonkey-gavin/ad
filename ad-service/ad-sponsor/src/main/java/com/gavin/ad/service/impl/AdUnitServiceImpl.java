package com.gavin.ad.service.impl;

import com.gavin.ad.constant.Constants;
import com.gavin.ad.dao.AdCreativeRepository;
import com.gavin.ad.dao.AdPlanRepository;
import com.gavin.ad.dao.AdUnitRepository;
import com.gavin.ad.dao.unit_condition.AdCreativeUnitRepository;
import com.gavin.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.gavin.ad.dao.unit_condition.AdUnitItRepository;
import com.gavin.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.gavin.ad.entity.AdPlan;
import com.gavin.ad.entity.AdUnit;
import com.gavin.ad.entity.unit_condition.AdCreativeUnit;
import com.gavin.ad.entity.unit_condition.AdUnitDistrict;
import com.gavin.ad.entity.unit_condition.AdUnitIt;
import com.gavin.ad.entity.unit_condition.AdUnitKeyword;
import com.gavin.ad.exception.AdException;
import com.gavin.ad.service.IAdUnitService;
import com.gavin.ad.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdUnitServiceImpl implements IAdUnitService {
    private AdPlanRepository adPlanRepository;
    private AdUnitRepository adUnitRepository;
    private AdUnitKeywordRepository adUnitKeywordRepository;
    private AdUnitItRepository adUnitItRepository;
    private AdUnitDistrictRepository adUnitDistrictRepository;
    private AdCreativeRepository adCreativeRepository;
    private AdCreativeUnitRepository adCreativeUnitRepository;

    @Autowired
    public AdUnitServiceImpl(AdPlanRepository adPlanRepository, AdUnitRepository adUnitRepository, AdUnitKeywordRepository adUnitKeywordRepository, AdUnitItRepository adUnitItRepository, AdUnitDistrictRepository adUnitDistrictRepository, AdCreativeRepository adCreativeRepository, AdCreativeUnitRepository adCreativeUnitRepository) {
        this.adPlanRepository = adPlanRepository;
        this.adUnitRepository = adUnitRepository;
        this.adUnitKeywordRepository = adUnitKeywordRepository;
        this.adUnitItRepository = adUnitItRepository;
        this.adUnitDistrictRepository = adUnitDistrictRepository;
        this.adCreativeRepository = adCreativeRepository;
        this.adCreativeUnitRepository = adCreativeUnitRepository;
    }

    @Override
    public AdUnitResponse createAdUnit(AdUnitRequest request) throws AdException {
        if (!request.createValidate()) {
            throw new AdException(Constants.ErrorMessage.REQUEST_PARAM_ERROR);
        }
        Optional<AdPlan> adPlan =
                adPlanRepository.findById(request.getPlanId());
        if (!adPlan.isPresent()) {
            throw new AdException(Constants.ErrorMessage.CAN_NOT_FIND_RECORD_ERROR);
        }
        AdUnit oldAdUnit = adUnitRepository.findByNameAndPlanId(
                request.getUnitName(), request.getPlanId()
        );
        if (oldAdUnit != null) {
            throw new AdException(Constants.ErrorMessage.SAME_UNIT_NAME_ERROR);
        }
        AdUnit newAdUnit = adUnitRepository.save(
                new AdUnit(request.getPlanId(), request.getUnitName(),
                        request.getPositionType(), request.getBudget())
        );

        return new AdUnitResponse(newAdUnit.getId(), newAdUnit.getUnitName());
    }

    @Override
    public AdUnitKeywordResponse createAdUnitKeyword(AdUnitKeywordRequest request) throws AdException {
        List<Long> unitIds = request.getUnitKeywords().stream().map(AdUnitKeywordRequest.UnitKeyword::getUnitId).collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMessage.REQUEST_PARAM_ERROR);
        }
        List<Long> ids = Collections.emptyList();
        List<AdUnitKeyword> unitKeywords = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitKeywords())) {
            request.getUnitKeywords().forEach(fe -> unitKeywords.add(new AdUnitKeyword(fe.getUnitId(), fe.getKeyword())));
            ids = adUnitKeywordRepository.saveAll(unitKeywords).stream().map(AdUnitKeyword::getId).collect(Collectors.toList());
        }
        return new AdUnitKeywordResponse(ids);
    }

    @Override
    public AdUnitItResponse createAdUnitIt(AdUnitItRequest request) throws AdException {
        List<Long> unitIds = request.getUnitIts().stream().map(AdUnitItRequest.UnitIt::getUnitId).collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMessage.REQUEST_PARAM_ERROR);
        }
        List<Long> ids = Collections.emptyList();
        List<AdUnitIt> unitIts = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitIts())) {
            request.getUnitIts().forEach(fe -> unitIts.add(new AdUnitIt(fe.getUnitId(), fe.getItTag())));
            ids = adUnitItRepository.saveAll(unitIts).stream().map(AdUnitIt::getId).collect(Collectors.toList());
        }
        return new AdUnitItResponse(ids);
    }

    @Override
    public AdUnitDistrictResponse createAdUnitDistrict(AdUnitDistrictRequest request) throws AdException {
        List<Long> unitIds = request.getUnitDistricts().stream().map(AdUnitDistrictRequest.UnitDistrict::getUnitId).collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMessage.REQUEST_PARAM_ERROR);
        }
        List<Long> ids = Collections.emptyList();
        List<AdUnitDistrict> unitDistricts = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitDistricts())) {
            request.getUnitDistricts().forEach(fe -> unitDistricts.add(new AdUnitDistrict(fe.getUnitId(), fe.getProvince(), fe.getCity())));
            ids = adUnitDistrictRepository.saveAll(unitDistricts).stream().map(AdUnitDistrict::getId).collect(Collectors.toList());
        }
        return new AdUnitDistrictResponse(ids);
    }

    @Override
    public AdCreativeUnitResponse createAdCreativeUnit(AdCreativeUnitRequest request) throws AdException {
        List<Long> unitIds = request.getUnitItems().stream()
                .map(AdCreativeUnitRequest.CreativeUnitItem::getUnitId)
                .collect(Collectors.toList());
        List<Long> creativeIds = request.getUnitItems().stream()
                .map(AdCreativeUnitRequest.CreativeUnitItem::getCreativeId)
                .collect(Collectors.toList());

        if (!(isRelatedUnitExist(unitIds) && isRelatedCreativeExist(creativeIds))) {
            throw new AdException(Constants.ErrorMessage.REQUEST_PARAM_ERROR);
        }

        List<AdCreativeUnit> creativeUnits = new ArrayList<>();
        request.getUnitItems().forEach(i -> creativeUnits.add(
                new AdCreativeUnit(i.getCreativeId(), i.getUnitId())
        ));

        List<Long> ids = adCreativeUnitRepository.saveAll(creativeUnits)
                .stream()
                .map(AdCreativeUnit::getId)
                .collect(Collectors.toList());

        return new AdCreativeUnitResponse(ids);
    }

    private boolean isRelatedUnitExist(List<Long> unitIds) {
        if (CollectionUtils.isEmpty(unitIds)) {
            return false;
        }
        return adUnitRepository.findAllById(unitIds).size() == new HashSet<>(unitIds).size();
    }

    private boolean isRelatedCreativeExist(List<Long> creativeIds) {

        if (CollectionUtils.isEmpty(creativeIds)) {
            return false;
        }
        return adCreativeRepository.findAllById(creativeIds).size() ==
                new HashSet<>(creativeIds).size();
    }
}
