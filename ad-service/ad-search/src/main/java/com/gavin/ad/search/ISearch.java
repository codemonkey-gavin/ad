package com.gavin.ad.search;

import com.gavin.ad.search.vo.SearchRequest;
import com.gavin.ad.search.vo.SearchResponse;

public interface ISearch {
    SearchResponse fetchAds(SearchRequest request);
}
