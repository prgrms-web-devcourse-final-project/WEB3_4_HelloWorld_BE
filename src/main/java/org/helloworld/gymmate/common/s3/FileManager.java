package org.helloworld.gymmate.common.s3;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileManager {
	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	//파일 업로드 관련
	/* 예시)
	 * folderPath :"images/profile"
	 * 이미지 경로(폴더 경로)를 key에 포함
	 *
	 * */
	public String saveFile(MultipartFile multipartFile, String folderPath) throws IOException {
		String originalFilename = multipartFile.getOriginalFilename();
		String fileName = UUID.randomUUID() + "_" + originalFilename;
		String key = folderPath + "/" + fileName;

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(multipartFile.getSize());
		metadata.setContentType(multipartFile.getContentType());

		amazonS3.putObject(bucket, key, multipartFile.getInputStream(), metadata);
		return amazonS3.getUrl(bucket, key).toString();
	}

	public ResponseEntity<UrlResource> downloadImage(String originalFilename, String folderPath) {
		String key = folderPath + "/" + originalFilename;
		UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, key));

		String contentDisposition = "attachment; filename=\"" + originalFilename + "\"";

		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
			.body(urlResource);
	}

	public void deleteImage(String originalFilename, String folderPath) {
		String key = folderPath + "/" + originalFilename;
		amazonS3.deleteObject(bucket, key);
	}
}
