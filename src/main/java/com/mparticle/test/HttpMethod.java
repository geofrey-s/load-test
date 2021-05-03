package com.mparticle.test;

import com.mparticle.test.Errors.InvalidHttpMethodException;

import java.util.Arrays;

public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE;

    public static HttpMethod from(String method) {
        return Arrays.stream(HttpMethod.values())
                .filter(_method -> _method.toString().equals(method.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> InvalidHttpMethodException.report(method));
    }
}
