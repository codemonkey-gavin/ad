package com.gavin.ad.search.impl;

import com.alibaba.fastjson.JSON;
import com.gavin.ad.index.CommonStatus;
import com.gavin.ad.index.DataTable;
import com.gavin.ad.index.adunit.AdUnitIndex;
import com.gavin.ad.index.adunit.AdUnitObject;
import com.gavin.ad.index.creative.AdCreativeIndex;
import com.gavin.ad.index.creative.AdCreativeObject;
import com.gavin.ad.index.creativeunit.AdCreativeUnitIndex;
import com.gavin.ad.index.district.UnitDistrictIndex;
import com.gavin.ad.index.interest.UnitItIndex;
import com.gavin.ad.index.keyword.UnitKeywordIndex;
import com.gavin.ad.search.ISearch;
import com.gavin.ad.search.vo.SearchRequest;
import com.gavin.ad.search.vo.SearchResponse;
import com.gavin.ad.search.vo.feature.DistrictFeature;
import com.gavin.ad.search.vo.feature.FeatureRelation;
import com.gavin.ad.search.vo.feature.ItFeature;
import com.gavin.ad.search.vo.feature.KeywordFeature;
import com.gavin.ad.search.vo.media.AdSlot;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class SearchImpl implements ISearch {
    @Override
    public SearchResponse fetchAds(SearchRequest request) {
        // 请求广告位信息
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();
        // Feature
        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();
        ItFeature itFeature = request.getFeatureInfo().getItFeature();
        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();
        FeatureRelation relation = request.getFeatureInfo().getRelation();

        // 构建响应对象
        SearchResponse response = new SearchResponse();
        Map<String, List<SearchResponse.Creative>> adSlotsAds = response.getAdSlotsAds();
        for (AdSlot adSlot : adSlots) {
            Set<Long> targetUnitIdSet;
            // 根据流量类型获取初始 AdUnit
            Set<Long> adUnitIdSet = DataTable.instance(AdUnitIndex.class).match(adSlot.getPositionType());
            if (relation == FeatureRelation.AND) {
                filterKeywordFeature(adUnitIdSet, keywordFeature);
                filterItFeature(adUnitIdSet, itFeature);
                filterDistrictFeature(adUnitIdSet, districtFeature);
                targetUnitIdSet = adUnitIdSet;
            } else {
                targetUnitIdSet = getOrRelationUnitIds(adUnitIdSet, keywordFeature, itFeature, districtFeature);
            }
            List<AdUnitObject> unitObjects = DataTable.instance(AdUnitIndex.class).fetch(targetUnitIdSet);
            filterAdUnitAndPlanStatus(unitObjects, CommonStatus.VALID);
            List<Long> adIds = DataTable.instance(AdCreativeUnitIndex.class).selectAds(unitObjects);
            List<AdCreativeObject> creatives = DataTable.instance(AdCreativeIndex.class).fetch(adIds);
            // 通过 AdSlot 实现对 CreativeObject 的过滤
            filterCreativeByAdSlot(creatives, adSlot.getWidth(), adSlot.getHeight(), adSlot.getType());

            adSlotsAds.put(adSlot.getAdSlotCode(), buildCreativeResponse(creatives));
        }
        log.info("fetchAds: {}-{}", JSON.toJSONString(request), JSON.toJSONString(response));
        return response;
    }

    private void filterKeywordFeature(Collection<Long> adUnitIds, KeywordFeature keywordFeature) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(keywordFeature.getKeywords())) {
            CollectionUtils.filter(adUnitIds, adUnitId -> DataTable.instance(UnitKeywordIndex.class).match(adUnitId, keywordFeature.getKeywords()));
        }
    }

    private void filterItFeature(Collection<Long> adUnitIds, ItFeature itFeature) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(itFeature.getIts())) {
            CollectionUtils.filter(adUnitIds, adUnitId -> DataTable.instance(UnitItIndex.class).match(adUnitId, itFeature.getIts()));
        }
    }

    private void filterDistrictFeature(Collection<Long> adUnitIds, DistrictFeature districtFeature) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(districtFeature.getDistricts())) {
            CollectionUtils.filter(adUnitIds, adUnitId -> DataTable.instance(UnitDistrictIndex.class).match(adUnitId, districtFeature.getDistricts()));
        }
    }

    private Set<Long> getOrRelationUnitIds(Set<Long> adUnitIdSet, KeywordFeature keywordFeature, ItFeature itFeature, DistrictFeature districtFeature) {
        if (CollectionUtils.isEmpty(adUnitIdSet)) {
            return Collections.emptySet();
        }
        Set<Long> keywordUnitIdsSet = new HashSet<>(adUnitIdSet);
        Set<Long> itUnitIdsSet = new HashSet<>(adUnitIdSet);
        Set<Long> districtUnitIdsSet = new HashSet<>(adUnitIdSet);

        filterKeywordFeature(keywordUnitIdsSet, keywordFeature);
        filterItFeature(itUnitIdsSet, itFeature);
        filterDistrictFeature(districtUnitIdsSet, districtFeature);

        return new HashSet<>(CollectionUtils.union(CollectionUtils.union(keywordUnitIdsSet, districtUnitIdsSet), itUnitIdsSet));
    }

    private void filterAdUnitAndPlanStatus(List<AdUnitObject> unitObjects, CommonStatus status) {
        if (CollectionUtils.isEmpty(unitObjects)) {
            return;
        }
        CollectionUtils.filter(unitObjects, object -> object.getUnitStatus().equals(status.getStatus()) && object.getAdPlanObject().getPlanStatus().equals(status.getStatus()));
    }

    private void filterCreativeByAdSlot(List<AdCreativeObject> creavies, Integer width, Integer height, List<Integer> type) {
        if (CollectionUtils.isEmpty(creavies)) {
            return;
        }
        CollectionUtils.filter(creavies, creative -> creative.getAuditStatus().equals(CommonStatus.VALID.getStatus()) && creative.getWidth().equals(width) && creative.getHeight().equals(height) && type.contains(creative.getType()));
    }

    private List<SearchResponse.Creative> buildCreativeResponse(List<AdCreativeObject> creavies) {
        if (CollectionUtils.isEmpty(creavies)) {
            return Collections.emptyList();
        }

        AdCreativeObject randomObject = creavies.get(Math.abs(new Random().nextInt()) % creavies.size());
        return Collections.singletonList(SearchResponse.convent(randomObject));
    }
}
