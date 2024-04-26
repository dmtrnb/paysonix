package com.example.paysonix.network;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleFormMapHttpMessageConverter implements HttpMessageConverter<Map<String, String>> {

    public static final Charset DEFAULT_CHARSET;
    private static final MediaType DEFAULT_FORM_DATA_MEDIA_TYPE;
    private final List<MediaType> supportedMediaTypes = new ArrayList<>();
    private final Charset charset;

    static {
        DEFAULT_CHARSET = StandardCharsets.UTF_8;
        DEFAULT_FORM_DATA_MEDIA_TYPE = new MediaType(MediaType.APPLICATION_FORM_URLENCODED, DEFAULT_CHARSET);
    }

    public SimpleFormMapHttpMessageConverter() {
        this.charset = DEFAULT_CHARSET;
        this.supportedMediaTypes.add(DEFAULT_FORM_DATA_MEDIA_TYPE);
        this.supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return Map.class.isAssignableFrom(clazz) && supportedMediaTypes.contains(mediaType);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return supportedMediaTypes;
    }

    @Override
    public Map<String, String> read(Class<? extends Map<String, String>> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        MediaType contentType = inputMessage.getHeaders().getContentType();
        Charset charset = contentType != null && contentType.getCharset() != null ? contentType.getCharset() : this.charset;
        String body = StreamUtils.copyToString(inputMessage.getBody(), charset);
        String[] pairs = StringUtils.tokenizeToStringArray(body, "&");
        Map<String, String> result = new HashMap<>(pairs.length);

        for (String pair : pairs) {
            int idx = pair.indexOf(61);
            if (idx == -1) {
                result.put(URLDecoder.decode(pair, charset), null);
            } else {
                String name = URLDecoder.decode(pair.substring(0, idx), charset);
                String value = URLDecoder.decode(pair.substring(idx + 1), charset);
                result.put(name, value);
            }
        }

        return result;
    }

    @Override
    public void write(Map<String, String> stringStringMap, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
    }
}
