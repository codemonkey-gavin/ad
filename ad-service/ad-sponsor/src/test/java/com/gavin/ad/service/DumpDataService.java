package com.gavin.ad.service;

import com.alibaba.fastjson.JSON;
import com.gavin.ad.TestApplication;
import com.gavin.ad.constant.CommonStatus;
import com.gavin.ad.dao.AdCreativeRepository;
import com.gavin.ad.dao.AdPlanRepository;
import com.gavin.ad.dao.AdUnitRepository;
import com.gavin.ad.dao.unit_condition.AdCreativeUnitRepository;
import com.gavin.ad.dao.unit_condition.AdUnitDistrictRepository;
import com.gavin.ad.dao.unit_condition.AdUnitItRepository;
import com.gavin.ad.dao.unit_condition.AdUnitKeywordRepository;
import com.gavin.ad.dump.DConstant;
import com.gavin.ad.dump.table.AdPlanTable;
import com.gavin.ad.dump.table.AdUnitTable;
import com.gavin.ad.entity.AdPlan;
import com.gavin.ad.entity.AdUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DumpDataService {
    @Autowired
    private AdPlanRepository planRepository;
    @Autowired
    private AdUnitRepository unitRepository;
    @Autowired
    private AdCreativeRepository creativeRepository;
    @Autowired
    private AdCreativeUnitRepository creativeUnitRepository;
    @Autowired
    private AdUnitDistrictRepository unitDistrictRepository;
    @Autowired
    private AdUnitKeywordRepository unitKeywordRepository;
    @Autowired
    private AdUnitItRepository unitItRepository;

    @Test
    public void dumpAdTableData() {
        dumpAdPlanTable(String.format("%s%s", DConstant.DATA_ROOT_PATH, DConstant.AD_PLAN));
        dumpAdUnitTable(String.format("%s%s", DConstant.DATA_ROOT_PATH, DConstant.AD_UNIT));
    }

    private void dumpAdPlanTable(String fileName) {
        List<AdPlan> adPlanList = planRepository.findAllByStatus(CommonStatus.VALID.getStatus());
        if (CollectionUtils.isEmpty(adPlanList)) {
            return;
        }
        List<AdPlanTable> planTableList = new ArrayList<>();
        adPlanList.forEach(f -> planTableList.add(
                new AdPlanTable(
                        f.getId(),
                        f.getUserId(),
                        f.getPlanStatus(),
                        f.getStartDate(),
                        f.getEndDate()
                )
        ));
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for (AdPlanTable plan : planTableList) {
                writer.write(JSON.toJSONString(plan));
                writer.newLine();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void dumpAdUnitTable(String fileName) {
        List<AdUnit> adUnitList = unitRepository.findAllByStatus(CommonStatus.VALID.getStatus());
        if (CollectionUtils.isEmpty(adUnitList)) {
            return;
        }
        List<AdUnitTable> unitTableList = new ArrayList<>();
        unitTableList.forEach(f -> unitTableList.add(
                new AdUnitTable(
                        f.getUnitId(),
                        f.getUnitStatus(),
                        f.getPositionType(),
                        f.getPlanId()
                )
        ));
        Path path = Paths.get(fileName);
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            for (AdUnitTable unit : unitTableList) {
                writer.write(JSON.toJSONString(unit));
                writer.newLine();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
