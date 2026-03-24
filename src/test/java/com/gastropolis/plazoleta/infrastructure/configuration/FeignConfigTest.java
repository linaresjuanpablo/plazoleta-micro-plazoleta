package com.gastropolis.plazoleta.infrastructure.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeignConfigTest {

    private FeignConfig feignConfig;

    @BeforeEach
    void setUp() {
        feignConfig = new FeignConfig();
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void requestInterceptor_returnsNonNullInterceptor() {
        RequestInterceptor interceptor = feignConfig.requestInterceptor();
        assertNotNull(interceptor);
    }

    @Test
    void requestInterceptor_whenNoRequestContext_doesNotAddHeader() {
        RequestContextHolder.resetRequestAttributes();
        RequestTemplate requestTemplate = mock(RequestTemplate.class);

        RequestInterceptor interceptor = feignConfig.requestInterceptor();
        interceptor.apply(requestTemplate);

        verify(requestTemplate, never()).header(anyString(), anyString());
    }

    @Test
    void requestInterceptor_whenRequestHasAuthorizationHeader_addsHeaderToTemplate() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer mytoken");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        RequestTemplate requestTemplate = mock(RequestTemplate.class);

        RequestInterceptor interceptor = feignConfig.requestInterceptor();
        interceptor.apply(requestTemplate);

        verify(requestTemplate).header("Authorization", "Bearer mytoken");
    }

    @Test
    void requestInterceptor_whenRequestHasNoAuthorizationHeader_doesNotAddHeader() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        RequestTemplate requestTemplate = mock(RequestTemplate.class);

        RequestInterceptor interceptor = feignConfig.requestInterceptor();
        interceptor.apply(requestTemplate);

        verify(requestTemplate, never()).header(anyString(), anyString());
    }
}
