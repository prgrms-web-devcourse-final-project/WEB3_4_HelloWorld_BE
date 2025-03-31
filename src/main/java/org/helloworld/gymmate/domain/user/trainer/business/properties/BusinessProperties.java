package org.helloworld.gymmate.domain.user.trainer.business.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "business")
@Getter
@Setter
public class BusinessProperties {
	private String url;
	private String serviceKey;
}
