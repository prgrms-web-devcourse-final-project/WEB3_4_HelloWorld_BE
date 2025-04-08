package org.helloworld.gymmate.domain.payment.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "payment")
public record PaymentProperties(
    String secretKey,
    String baseUrl,
    String confirmUrl
) {
}
