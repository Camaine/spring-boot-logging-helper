package org.springlogginghelper;


import org.springframework.util.StreamUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;

public class CachingRequestBodyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(httpServletRequest);
        String requestBody = StreamUtils.copyToString(cachedBodyHttpServletRequest.getInputStream(), Charset.defaultCharset());
        request.setAttribute("requestBody", requestBody);
        chain.doFilter(cachedBodyHttpServletRequest, response);
    }
}

