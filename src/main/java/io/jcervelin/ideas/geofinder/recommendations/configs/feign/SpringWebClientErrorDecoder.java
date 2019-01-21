package io.jcervelin.ideas.geofinder.recommendations.configs.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.io.ByteStreams.toByteArray;
import static org.springframework.http.HttpStatus.valueOf;

public class SpringWebClientErrorDecoder implements ErrorDecoder {

    private ErrorDecoder delegate = new Default();

    @Override
    public Exception decode(final String methodKey, final Response response) {
        final HttpStatus statusCode = valueOf(response.status());

        if (statusCode.is4xxClientError()) {
            return new HttpClientErrorException(
                    statusCode, response.reason(), responseHeaders(response), responseBody(response), null);
        }

        if (statusCode.is5xxServerError()) {
            return new HttpServerErrorException(
                    statusCode, response.reason(), responseHeaders(response), responseBody(response), null);
        }

        return delegate.decode(methodKey, response);
    }

    private HttpHeaders responseHeaders(final Response response) {
        final HttpHeaders headers = new HttpHeaders();
        response
                .headers()
                .entrySet()
                .forEach(entry -> headers.put(entry.getKey(), newArrayList(entry.getValue())));
        return headers;
    }

    private byte[] responseBody(final Response response) {
        return Optional
                .ofNullable(response.body())
                .map(body -> {
                    try {
                        return toByteArray(body.asInputStream());
                    } catch (IOException e) {
                        throw new HttpMessageNotReadableException("Failed to process response body.", e);
                    }
                })
                .orElse(new byte[0]);
    }

}