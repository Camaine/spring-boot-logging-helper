package org.springlogginghelper.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springlogginghelper.CachingRequestBodyFilter;

@Configuration
public class FilterConfiguration {
    @Bean
    public CachingRequestBodyFilter cachingRequestBodyFilter() {
        return new CachingRequestBodyFilter();
    }

    @Bean
    public FilterRegistrationBean<CachingRequestBodyFilter> cachingRequestBodyFilterBean(CachingRequestBodyFilter cachingRequestBodyFilter) {
        FilterRegistrationBean<CachingRequestBodyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(cachingRequestBodyFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
