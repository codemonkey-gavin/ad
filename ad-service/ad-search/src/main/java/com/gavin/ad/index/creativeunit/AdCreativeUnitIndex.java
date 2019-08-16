package com.gavin.ad.index.creativeunit;

import com.gavin.ad.index.IndexAware;
import com.gavin.ad.index.adunit.AdUnitObject;
import com.gavin.ad.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class AdCreativeUnitIndex implements IndexAware<String, AdCreativeUnitObject> {
    // <id-unitid, AdCreativeUnitObject>
    private static Map<String, AdCreativeUnitObject> objectMap;
    // <id,unitid set>
    private static Map<Long, Set<Long>> creativeUnitMap;
    // <unitid,id set>
    private static Map<Long, Set<Long>> unitCreativeMap;

    public List<Long> selectAds(List<AdUnitObject> unitObjects) {

        if (CollectionUtils.isEmpty(unitObjects)) {
            return Collections.emptyList();
        }

        List<Long> result = new ArrayList<>();

        for (AdUnitObject unitObject : unitObjects) {

            Set<Long> adIds = unitCreativeMap.get(unitObject.getUnitId());
            if (CollectionUtils.isNotEmpty(adIds)) {
                result.addAll(adIds);
            }
        }

        return result;
    }

    @Override
    public AdCreativeUnitObject get(String key) {
        return objectMap.get(key);
    }

    @Override
    public void add(String key, AdCreativeUnitObject value) {
        log.info("before add：{}", objectMap);
        objectMap.put(key, value);
        Set<Long> unitSet = creativeUnitMap.get(value.getId());
        if (CollectionUtils.isEmpty(unitSet)) {
            unitSet = new ConcurrentSkipListSet<>();
            creativeUnitMap.put(value.getId(), unitSet);
        }
        unitSet.add(value.getUnitId());

        Set<Long> creativeSet = unitCreativeMap.get(value.getUnitId());
        if (CollectionUtils.isEmpty(creativeSet)) {
            creativeSet = new ConcurrentSkipListSet<>();
            unitCreativeMap.put(value.getUnitId(), creativeSet);
        }
        creativeSet.add(value.getId());
        log.info("after add：{}", objectMap);
    }

    @Override
    public void update(String key, AdCreativeUnitObject value) {
        log.error("creativeUnit index can not support update");
    }

    @Override
    public void delete(String key, AdCreativeUnitObject value) {
        log.info("before delete：{}", objectMap);
        objectMap.remove(key);
        Set<Long> unitSet=creativeUnitMap.get(value.getId());
        if(CollectionUtils.isNotEmpty(unitSet)){
            unitSet.remove(value.getUnitId());
        }
        Set<Long> creativeSet=unitCreativeMap.get(value.getUnitId());
        if(CollectionUtils.isNotEmpty(creativeSet)){
            creativeSet.remove(value.getId());
        }
        log.info("after delete：{}", objectMap);
    }
}
