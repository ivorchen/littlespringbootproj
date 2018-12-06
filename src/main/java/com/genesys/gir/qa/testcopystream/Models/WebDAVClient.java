package com.genesys.gir.qa.testcopystream.Models;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.genesys.gir.qa.testcopystream.Controllers.ApiController;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.security.crypto.codec.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URI;

public class WebDAVClient {
    private Logger logger = LogManager.getLogger(ApiController.class);

    private static final String USER_AGENT = "GenesysWebDav/1.0";

    private final CloseableHttpClient httpClient;

    public WebDAVClient(HttpClientConnectionManager cm, int socketTimeout) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setExpectContinueEnabled(false)
                .setSocketTimeout(socketTimeout)
                .build();

        httpClient = HttpClients.custom()
                .useSystemProperties()
                .setConnectionManager(cm)
                .setUserAgent(USER_AGENT)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public InputStream get(final URI mediaPath, final String userName, final String password) throws IOException {
        final CloseableHttpResponse mediaObject = getMediaObject(mediaPath, userName, password, new HashMap<>());
        return getObjectContent(mediaObject, mediaPath);
    }

    private CloseableHttpResponse getMediaObject(final URI mediaPath, final String userName, final String password, final Map<String, String> headers) throws IOException {
        HttpGet request = new HttpGet(mediaPath);
        request.addHeader("Authorization", getAuthorizationHeader(userName, password));

        for (Entry<String, String> header : headers.entrySet()) {
            request.addHeader(header.getKey(), header.getValue());
        }

        try {
            logger.debug("Ready to send requests");
            final CloseableHttpResponse response = executeRequest(request);
            final int httpStatus = response.getStatusLine().getStatusCode();
            logger.debug("Processing Response");
            if (httpStatus != HttpStatus.SC_OK && httpStatus != HttpStatus.SC_PARTIAL_CONTENT) {
                HttpClientUtils.closeQuietly(response);
                throw new IOException("Media file cannot be retrieved " + mediaPath + "; server responses: " + response);
            }
            return response;
        } catch (IOException e) {
            throw new IOException("Failed to fetch media file: " + mediaPath, e);
        }
    }

    private InputStream getObjectContent(final CloseableHttpResponse response, final URI mediaPath) throws IOException {
        try {
            logger.debug("Returning input stream from webdav");
            return response.getEntity().getContent();
        } catch (IOException e) {
            HttpClientUtils.closeQuietly(response);
            throw new IOException("Failed to read media content: " + mediaPath, e);
        }
    }

    private String getAuthorizationHeader(final String userName, final String password) {
        final String keyPair = userName + ":" + password;
        String encoding = new String(Base64.encode(keyPair.getBytes()));
        return "Basic " + encoding;
    }

    private CloseableHttpResponse executeRequest(HttpUriRequest request) throws IOException {
        return httpClient.execute(request);
    }
}

