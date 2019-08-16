package com.gavin.ad.controller;

import com.gavin.ad.search.ISearch;
import com.gavin.ad.search.vo.SearchRequest;
import com.gavin.ad.search.vo.SearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SearchController {

    private final ISearch search;

    @Autowired
    public SearchController(ISearch search) {
        this.search = search;
    }

    @PostMapping("/fetchAds")
    public SearchResponse fetchAds(@RequestBody SearchRequest request) {
        return search.fetchAds(request);
    }
}
