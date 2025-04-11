package org.helloworld.gymmate.infra.dto;

import java.util.List;

public record KakaoAddressResponse(
    List<Document> documents
) {
    public record Document(
        String address_name,
        String x,  // longitude
        String y   // latitude
    ) {
    }
}