package org.helloworld.gymmate.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloud.aws")
public record AwsS3Properties(S3 s3, Credentials credentials, Region region) {

	public record S3(String bucket) {
	}

	public record Credentials(String accessKey, String secretKey) {
	}

	public record Region(String staticRegion) {
	}
}