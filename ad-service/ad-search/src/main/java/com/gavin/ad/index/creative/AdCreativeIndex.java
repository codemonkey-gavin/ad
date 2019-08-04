package com.gavin.ad.index.creative;

import com.gavin.ad.index.IndexAware;
import com.gavin.ad.index.adunit.AdUnitObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class AdCreativeIndex implements IndexAware<Long, AdCreativeObject> {
    private static Map<Long, AdCreativeObject> objectMap;

    static {
        objectMap = new ConcurrentHashMap<>();
    }

    @Override
    public AdCreativeObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdCreativeObject value) {
        log.info("before add：{}", objectMap);
        objectMap.put(key, value);
        log.info("after add：{}", objectMap);
    }

    @Override
    public void update(Long key, AdCreativeObject value) {
        log.info("before update：{}", objectMap);
        AdCreativeObject oldObject = objectMap.get(key);
        if (null == oldObject) {
            objectMap.put(key, value);
        } else {
            oldObject.update(value);
        }
        log.info("after update：{}", objectMap);
    }

    @Override
    public void delete(Long key, AdCreativeObject value) {
        log.info("before delete：{}", objectMap);
        objectMap.remove(key);
        log.info("after delete：{}", objectMap);
    }
}
