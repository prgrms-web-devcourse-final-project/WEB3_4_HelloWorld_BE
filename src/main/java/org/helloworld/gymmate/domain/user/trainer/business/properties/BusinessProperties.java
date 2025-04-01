package org.helloworld.gymmate.domain.user.trainer.business.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "business")
public record BusinessProperties(String url, String serviceKey) {
}