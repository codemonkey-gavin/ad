package com.gavin.ad.service.impl;

import com.gavin.ad.constant.CommonStatus;
import com.gavin.ad.constant.Constants;
import com.gavin.ad.dao.AdPlanRepository;
import com.gavin.ad.dao.AdUserRepository;
import com.gavin.ad.entity.AdPlan;
import com.gavin.ad.entity.AdUser;
import com.gavin.ad.exception.AdException;
import com.gavin.ad.service.IAdPlanService;
import com.gavin.ad.utils.CommonUtils;
import com.gavin.ad.vo.AdPlanGetRequest;
import com.gavin.ad.vo.AdPlanRequest;
import com.gavin.ad.vo.AdPlanResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdPlanServiceImpl implements IAdPlanService {
    private AdUserRepository adUserRepository;
    private AdPlanRepository adPlanRepository;

    @Autowired
    public AdPlanServiceImpl(AdUserRepository adUserRepository, AdPlanRepository adPlanRepository) {
        this.adUserRepository = adUserRepository;
        this.adPlanRepository = adPlanRepository;
    }

    @Override
    @Transactional
    public AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException {
        if (!request.createValidate()) {
            throw new AdException(Constants.ErrorMessage.REQUEST_PARAM_ERROR);
        }
        Optional<AdUser> user = adUserRepository.findById(request.getUserId());
        if (user.isPresent()) {
            throw new AdException(Constants.ErrorMessage.CAN_NOT_FIND_RECORD_ERROR);
        }
        AdPlan oldPlan = adPlanRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (null != oldPlan) {
            throw new AdException(Constants.ErrorMessage.SAME_PLAN_NAME_ERROR);
        }
        AdPlan newPlan = adPlanRepository.save(new AdPlan(request.getUserId(), request.getPlanName(), CommonUtils.parseStringDate(request.getStartDate()), CommonUtils.parseStringDate(request.getEndDate())));
        return new AdPlanResponse(newPlan.getId(), newPlan.getPlanName());
    }

    @Override
    @Transactional
    public AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException {
        if (!request.updateValidate()) {
            throw new AdException(Constants.ErrorMessage.REQUEST_PARAM_ERROR);
        }
        AdPlan plan = adPlanRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (null != plan) {
            throw new AdException(Constants.ErrorMessage.SAME_PLAN_NAME_ERROR);
        }
        if (null != request.getPlanName()) {
            plan.setPlanName(request.getPlanName());
        }
        if (null != request.getStartDate()) {
            plan.setStartDate(CommonUtils.parseStringDate(request.getStartDate()));
        }
        if (null != request.getEndDate()) {
            plan.setEndDate(CommonUtils.parseStringDate(request.getEndDate()));
        }
        plan.setUpdateTime(new Date());
        plan = adPlanRepository.save(plan);
        return new AdPlanResponse(plan.getId(), plan.getPlanName());
    }

    @Override
    public List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException {
        if (request.validate()) {
            throw new AdException(Constants.ErrorMessage.REQUEST_PARAM_ERROR);
        }
        return adPlanRepository.findAllByIdsAndUserId(request.getIds(), request.getUserId());
    }

    @Override
    @Transactional
    public void deleteAdPlan(AdPlanRequest request) throws AdException {
        if (!request.deleteValidate()) {
            throw new AdException(Constants.ErrorMessage.REQUEST_PARAM_ERROR);
        }
        AdPlan plan = adPlanRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (null != plan) {
            throw new AdException(Constants.ErrorMessage.SAME_PLAN_NAME_ERROR);
        }
        plan.setPlanStatus(CommonStatus.INVALID.getStatus());
        plan.setUpdateTime(new Date());
        adPlanRepository.save(plan);
    }
}
