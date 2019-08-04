package com.gavin.ad.dao;

import com.gavin.ad.entity.AdUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdUnitRepository extends JpaRepository<AdUnit, Long> {
    AdUnit findByNameAndPlanId(String name, Long planId);

    List<AdUnit> findAllByStatus(Integer status);
}
