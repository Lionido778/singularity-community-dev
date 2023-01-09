package org.n3r.idworker.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author Lionido
 */
public class HttpReq {
    private static final String ENCODE_FORMAT = "UTF-8";
    private final String baseUrl;
    private String req;
    private final StringBuilder params = new StringBuilder();
    Logger logger = LoggerFactory.getLogger(HttpReq.class);

    public HttpReq(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static HttpReq get(String baseUrl) {
        return new HttpReq(baseUrl);
    }

    public HttpReq req(String req) {
        this.req = req;
        return this;
    }

    public HttpReq param(String name, String value) {
        if (params.length() > 0) {
            params.append('&');
        }
        try {
            params.append(name).append('=').append(URLEncoder.encode(value, ENCODE_FORMAT));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public String exec() {
        HttpURLConnection http = null;
        try {
            http = (HttpURLConnection) new URL(baseUrl
                    + (req == null ? "" : req)
                    + (params.length() > 0 ? ("?" + params) : "")).openConnection();
            http.setRequestProperty("Accept-Charset", ENCODE_FORMAT);
            HttpURLConnection.setFollowRedirects(false);
            http.setConnectTimeout(5 * 1000);
            http.setReadTimeout(5 * 1000);
            http.connect();

            int status = http.getResponseCode();
            String charset = getCharset(http.getHeaderField("Content-Type"));

            if (status == 200) {
                return readResponseBody(http, charset);
            } else {
                logger.warn("non 200 respoonse :" + readErrorResponseBody(http, status, charset));
                return null;
            }
        } catch (Exception e) {
            logger.error("exec error {}", e.getMessage());
            return null;
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }

    }

    private static String readErrorResponseBody(HttpURLConnection http, int status, String charset) throws IOException {
        InputStream errorStream = http.getErrorStream();
        if (errorStream != null) {
            String error = toString(charset, errorStream);
            return ("STATUS CODE =" + status + "\n\n" + error);
        } else {
            return ("STATUS CODE =" + status);
        }
    }

    private static String readResponseBody(HttpURLConnection http, String charset) throws IOException {
        InputStream inputStream = http.getInputStream();

        return toString(charset, inputStream);
    }

    private static String toString(String charset, InputStream inputStream) throws IOException {
        ByteArrayOutputStream bass = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            bass.write(buffer, 0, length);
        }

        return new String(bass.toByteArray(), charset);
    }

    private static String getCharset(String contentType) {
        if (contentType == null) {
            return ENCODE_FORMAT;
        }

        String charset = null;
        for (String param : contentType.replace(" ", "").split(";")) {
            if (param.startsWith("charset=")) {
                charset = param.split("=", 2)[1];
                break;
            }
        }

        return charset == null ? ENCODE_FORMAT : charset;
    }
}
