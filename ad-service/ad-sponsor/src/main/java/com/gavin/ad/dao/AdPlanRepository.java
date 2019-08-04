package com.gavin.ad.dao;

import com.gavin.ad.entity.AdPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdPlanRepository extends JpaRepository<AdPlan, Long> {
    AdPlan findByIdAndUserId(Long id, Long userId);

    AdPlan findByNameAndUserId(String name, Long userId);

    List<AdPlan> findAllByIdsAndUserId(List<Long> ids, Long userId);

    List<AdPlan> findAllByStatus(Integer status);
}
