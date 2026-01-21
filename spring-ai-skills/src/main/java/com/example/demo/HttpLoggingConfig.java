package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Configuration
public class HttpLoggingConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpLoggingConfig.class);

  @Bean
  RestClient.Builder restClientBuilder() {
    ClientHttpRequestInterceptor interceptor = (req, reqBody, ex) -> {
      LOGGER.info("Request body: \n===========\n{}\n===========", new String(reqBody, StandardCharsets.UTF_8));
      ClientHttpResponse response = ex.execute(req, reqBody);
      final ClientHttpResponse responseCopy = new BufferingClientHttpResponseWrapper(response);
      InputStreamReader isr = new InputStreamReader(
          responseCopy.getBody(), StandardCharsets.UTF_8);
      String body = new BufferedReader(isr).lines()
          .collect(Collectors.joining("\n"));
      LOGGER.info("Response body:\n===========\n{}\n===========", body);
      return responseCopy;
    };

    return RestClient.builder()
        .requestInterceptor(interceptor);
  }

  @SuppressWarnings("NullableProblems")
  final static class BufferingClientHttpResponseWrapper implements ClientHttpResponse {
    private final ClientHttpResponse response;
    @Nullable
    private byte[] body;

    BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
      this.response = response;
    }

    @Override
    public HttpStatusCode getStatusCode() throws IOException {
      return this.response.getStatusCode();
    }

    @Override
    public String getStatusText() throws IOException {
      return this.response.getStatusText();
    }

    @Override
    public HttpHeaders getHeaders() {
      return this.response.getHeaders();
    }

    @Override
    public InputStream getBody() throws IOException {
      if (this.body == null) {
        this.body = StreamUtils.copyToByteArray(this.response.getBody());
      }
      return new ByteArrayInputStream(this.body);
    }

    @Override
    public void close() {
      this.response.close();
    }
  }
}
