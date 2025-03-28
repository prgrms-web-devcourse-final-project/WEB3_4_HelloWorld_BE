package org.helloworld.gymmate.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cloud.aws")
public class AwsS3Properties {
	private S3 s3;
	private Credentials credentials;
	private Region region;

	@Getter
	@Setter
	public static class S3 {
		private String bucket;
	}

	@Getter
	@Setter
	public static class Credentials {
		private String accessKey;
		private String secretKey;
	}

	@Getter
	@Setter
	public static class Region {
		private String staticRegion;
	}
}