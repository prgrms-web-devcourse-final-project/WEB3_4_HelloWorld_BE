package org.helloworld.gymmate.common.config;

import org.helloworld.gymmate.common.properties.AwsS3Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class S3Config {
	private final AwsS3Properties awsS3Properties;

	@Bean
	public S3Client s3Client() {
		return S3Client.builder()
			.region(Region.of(awsS3Properties.getRegion().getStaticRegion()))
			.credentialsProvider(StaticCredentialsProvider.create(
				AwsBasicCredentials.create(
					awsS3Properties.getCredentials().getAccessKey(),
					awsS3Properties.getCredentials().getSecretKey()
				)
			))
			.build();
	}
}
