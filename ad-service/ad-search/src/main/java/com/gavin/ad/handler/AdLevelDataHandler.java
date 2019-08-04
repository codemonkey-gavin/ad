package com.gavin.ad.handler;

import com.alibaba.fastjson.JSON;
import com.gavin.ad.dump.table.*;
import com.gavin.ad.index.DataTable;
import com.gavin.ad.index.IndexAware;
import com.gavin.ad.index.adplan.AdPlanIndex;
import com.gavin.ad.index.adplan.AdPlanObject;
import com.gavin.ad.index.adunit.AdUnitIndex;
import com.gavin.ad.index.adunit.AdUnitObject;
import com.gavin.ad.index.creative.AdCreativeIndex;
import com.gavin.ad.index.creative.AdCreativeObject;
import com.gavin.ad.index.creativeunit.AdCreativeUnitIndex;
import com.gavin.ad.index.creativeunit.AdCreativeUnitObject;
import com.gavin.ad.index.district.UnitDistrictIndex;
import com.gavin.ad.index.interest.UnitItIndex;
import com.gavin.ad.index.keyword.UnitKeywordIndex;
import com.gavin.ad.mysql.constant.OpType;
import com.gavin.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class AdLevelDataHandler {
    public static void handlerlevel2(AdPlanTable planTable, OpType type) {
        AdPlanObject planObject = new AdPlanObject(
                planTable.getPlanId(),
                planTable.getUserId(),
                planTable.getPlanStatus(),
                planTable.getStartDate(),
                planTable.getEndDate()
        );
        handlerBinlogEvent(DataTable.instance(AdPlanIndex.class), planObject.getPlanId(), planObject, type);
    }

    public static void handlerlevel2(AdCreativeTable creativeTable, OpType type) {

        AdCreativeObject creativeObject = new AdCreativeObject(
                creativeTable.getId(),
                creativeTable.getName(),
                creativeTable.getType(),
                creativeTable.getMaterialType(),
                creativeTable.getHeight(),
                creativeTable.getWidth(),
                creativeTable.getAuditStatus(),
                creativeTable.getUrl()
        );
        handlerBinlogEvent(
                DataTable.instance(AdCreativeIndex.class),
                creativeObject.getId(),
                creativeObject,
                type
        );
    }

    public static void handlerlevel3(AdUnitTable unitTable, OpType type) {
        AdPlanObject planObject = DataTable.instance(AdPlanIndex.class).get(unitTable.getPlanId());
        if (null == planObject) {
            return;
        }
        AdUnitObject unitObject = new AdUnitObject(
                unitTable.getUnitId(),
                unitTable.getUnitStatus(),
                unitTable.getPositionType(),
                unitTable.getPlanId(),
                planObject
        );
        handlerBinlogEvent(DataTable.instance(AdUnitIndex.class), unitObject.getUnitId(), unitObject, type);
    }

    public static void handlerlevel3(AdCreativeUnitTable creativeUnitTable, OpType type) {

        if (type == OpType.UPDATE) {
            log.error("CreativeUnitIndex not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.instance(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        AdCreativeObject creativeObject = DataTable.instance(
                AdCreativeIndex.class).get(creativeUnitTable.getId());

        if (null == unitObject || null == creativeObject) {
            log.error("AdCreativeUnitTable index error: {}", JSON.toJSONString(creativeUnitTable));
            return;
        }

        AdCreativeUnitObject creativeUnitObject = new AdCreativeUnitObject(
                creativeUnitTable.getId(),
                creativeUnitTable.getUnitId()
        );
        handlerBinlogEvent(
                DataTable.instance(AdCreativeUnitIndex.class), CommonUtils.stringConcat(
                        creativeUnitObject.getId().toString(), creativeUnitObject.getUnitId().toString()
                ),
                creativeUnitObject,
                type
        );
    }

    public static void handlerLevel4(AdUnitDistrictTable unitDistrictTable, OpType type) {

        if (type == OpType.UPDATE) {
            log.error("district index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.instance(
                AdUnitIndex.class
        ).get(unitDistrictTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitDistrictTable index error: {}",
                    unitDistrictTable.getUnitId());
            return;
        }

        String key = CommonUtils.stringConcat(
                unitDistrictTable.getProvince(),
                unitDistrictTable.getCity()
        );
        Set<Long> value = new HashSet<>(Collections.singleton(unitDistrictTable.getUnitId())
        );
        handlerBinlogEvent(
                DataTable.instance(UnitDistrictIndex.class),
                key, value,
                type
        );
    }

    public static void handlerLevel4(AdUnitItTable unitItTable, OpType type) {

        if (type == OpType.UPDATE) {
            log.error("it index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.instance(
                AdUnitIndex.class
        ).get(unitItTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitItTable index error: {}",
                    unitItTable.getUnitId());
            return;
        }

        Set<Long> value = new HashSet<>(
                Collections.singleton(unitItTable.getUnitId())
        );
        handlerBinlogEvent(
                DataTable.instance(UnitItIndex.class),
                unitItTable.getItTag(),
                value,
                type
        );
    }

    public static void handlerLevel4(AdUnitKeywordTable keywordTable,
                                     OpType type) {

        if (type == OpType.UPDATE) {
            log.error("keyword index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.instance(
                AdUnitIndex.class
        ).get(keywordTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitKeywordTable index error: {}",
                    keywordTable.getUnitId());
            return;
        }

        Set<Long> value = new HashSet<>(
                Collections.singleton(keywordTable.getUnitId())
        );
        handlerBinlogEvent(
                DataTable.instance(UnitKeywordIndex.class),
                keywordTable.getKeyword(),
                value,
                type
        );
    }

    private static <K, V> void handlerBinlogEvent(IndexAware<K, V> index, K key, V value, OpType type) {
        switch (type) {
            case ADD:
                index.add(key, value);
                break;
            case UPDATE:
                index.update(key, value);
                break;
            case DELETE:
                index.delete(key, value);
                break;
            default:
                break;
        }
    }
}
