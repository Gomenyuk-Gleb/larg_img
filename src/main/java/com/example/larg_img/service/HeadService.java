package com.example.larg_img.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;
import java.util.Optional;

@Service
public class HeadService {


    RestTemplate restTemplate = new RestTemplate();

    public String largestURLIMG(String sol) throws JsonProcessingException {
        String buildURL = buildURL(sol);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(buildURL, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(forEntity.getBody());
        Optional<Pair<String, Long>> img_src = jsonNode.findValues("img_src").parallelStream()
                .map(x -> getHeader(x.asText()))
                .max((o1, o2) -> o1.getValue().compareTo(o2.getValue()));

        return img_src.get().getKey();
    }

    public String buildURL(String sol) {
        return "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=" + sol + "&api_key=" + "QahbcQ7w6KWC70JpbB5Qtvgzz1PYiEvCo8yYl6V4";
    }

    public Pair<String, Long> getHeader(String url) {
        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        final HttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
        factory.setHttpClient(httpClient);
        restTemplate.setRequestFactory(factory);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);

        return Pair.of(url, forEntity.getHeaders().getContentLength());
    }
}
