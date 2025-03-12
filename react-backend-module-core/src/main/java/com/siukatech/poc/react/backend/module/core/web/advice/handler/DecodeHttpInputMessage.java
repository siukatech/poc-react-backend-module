package com.siukatech.poc.react.backend.module.core.web.advice.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.IOException;
import java.io.InputStream;

/**
 * Reference:
 * https://www.springcloud.io/post/2022-03/spring-mvc-decode-body/#gsc.tab=0
 */
public class DecodeHttpInputMessage implements HttpInputMessage {
    private HttpHeaders httpHeaders;
    private InputStream body;

    public DecodeHttpInputMessage(HttpHeaders httpHeaders, InputStream body) {
        super();
        this.httpHeaders = httpHeaders;
        this.body = body;
    }

    @Override
    public InputStream getBody() throws IOException {
        return this.body;
    }

    @Override
    public HttpHeaders getHeaders() {
        return this.httpHeaders;
    }
}
