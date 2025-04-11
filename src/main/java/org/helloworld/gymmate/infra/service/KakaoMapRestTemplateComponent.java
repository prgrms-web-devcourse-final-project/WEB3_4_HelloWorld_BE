package org.helloworld.gymmate.infra.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.helloworld.gymmate.infra.dto.KakaoAddressResponse;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoMapRestTemplateComponent {
    private final RestTemplate restTemplate;

    @Value("${kakao-map.api.key}")
    private String kakaoApiKey;

    public Optional<Coordinate> getCoordinatesFromAddress(String address) {
        log.debug("주소 x y좌표 변환 실행");

        String cleanedAddress = address
            .replaceAll("\\(.*?\\)", "")
            .replaceAll("\\s+", " ")
            .trim();

        String encodedQuery = URLEncoder.encode(cleanedAddress, StandardCharsets.UTF_8);

        String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + encodedQuery;
        URI uri = URI.create(url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoAddressResponse> response = restTemplate.exchange(
            uri, HttpMethod.GET, entity, KakaoAddressResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK &&
            response.getBody() != null &&
            response.getBody().documents() != null &&
            !response.getBody().documents().isEmpty()) {

            KakaoAddressResponse.Document doc = response.getBody().documents().getFirst();
            return Optional.of(new Coordinate(Double.parseDouble(doc.x()), Double.parseDouble(doc.y())));
        }
        return Optional.empty();
    }
}
