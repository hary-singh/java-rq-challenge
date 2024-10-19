package com.example.rqchallenge.util;

import org.springframework.http.HttpHeaders;

/**
 * Utility class for creating HTTP headers.
 */
public class HttpHeaderUtil {

    // Removed hardcoded cookie for security reasons.
    // The API has bot prevention mechanisms, so a valid cookie is required for requests.
    // To test, I had to visit the website via a browser to obtain a valid cookie and set it here.
    private static final String COOKIE = "redacted-cookie";

    /**
     * Creates HTTP headers with a cookie.
     */
    public static HttpHeaders createHeadersWithCookie() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", COOKIE);
        return headers;
    }

}
