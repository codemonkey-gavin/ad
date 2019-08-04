package com.gavin.ad.service;

import com.gavin.ad.entity.AdPlan;
import com.gavin.ad.exception.AdException;
import com.gavin.ad.vo.AdPlanGetRequest;
import com.gavin.ad.vo.AdPlanRequest;
import com.gavin.ad.vo.AdPlanResponse;

import java.util.List;

public interface IAdPlanService {
    /**
     * 创建推广计划
     *
     * @param request
     * @return
     * @throws AdException
     */
    AdPlanResponse createAdPlan(AdPlanRequest request) throws AdException;

    /**
     * 更新推广计划
     *
     * @param request
     * @return
     * @throws AdException
     */
    AdPlanResponse updateAdPlan(AdPlanRequest request) throws AdException;

    /**
     * 获取推广计划
     *
     * @param request
     * @return
     * @throws AdException
     */
    List<AdPlan> getAdPlanByIds(AdPlanGetRequest request) throws AdException;

    /**
     * 删除推广计划
     *
     * @param request
     * @throws AdException
     */
    void deleteAdPlan(AdPlanRequest request) throws AdException;
}
