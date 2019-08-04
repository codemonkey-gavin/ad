package com.gavin.ad.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class AccessLogFilter extends ZuulFilter {
    @Override
    public String filterType() {
        // 过滤器的类型
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        // zuul 组件默认的 response filter 的 order 是 1000（因为zuul的FilterConstants.SEND_RESPONSE_FILTER_ORDER
        // 常量就是1000），所以我们的 response 过滤器的 order要在 FilterConstants.SEND_RESPONSE_FILTER_ORDER 之前，否则不起作用。
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        // 是否启用当前的过滤器
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String uri = request.getRequestURI();
        long startTime = (long) context.get("startTime");
        long duration = System.currentTimeMillis() - startTime;
        log.info("uri: " + uri + ", duration: " + duration / 100 + "ms");
        return null;
    }
}
