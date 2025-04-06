package org.helloworld.gymmate.common.s3;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.helloworld.gymmate.common.properties.AwsS3Properties;
import org.helloworld.gymmate.common.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class FileManager {
	private final S3Client s3Client;
	private final AwsS3Properties awsS3Properties;

	// 단일업로드
	public String uploadFile(MultipartFile file, String tableName) {
		return uploadToS3(file, tableName);
	}

	// 여러파일업로드
	public List<String> uploadFiles(List<MultipartFile> files, String tableName) {
		return files.stream()
			.map(file -> uploadToS3(file, tableName))
			.collect(Collectors.toList());
	}

	// 테이블별 파일 저장 기능
	public String uploadToS3(MultipartFile file, String tableName) {
		String bucketName = awsS3Properties.s3().bucket();

		LocalDate now = LocalDate.now();
		String datePath = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String extension = getFileExtension(file.getOriginalFilename());
		String uuidFileName = UUID.randomUUID() + extension;

		// 최종 S3 저장 경로 (도메인명/년월일/UUID.확장자)
		String fileName = StringUtil.format("{}/{}/{}", tableName, datePath, uuidFileName);

		try {
			PutObjectRequest request = PutObjectRequest.builder()
				.bucket(bucketName)
				.key(fileName)
				.build();

			s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

			return "https://" + bucketName + ".s3." +
				awsS3Properties.region().staticRegion() + ".amazonaws.com/" + fileName;
		} catch (IOException e) {
			throw new RuntimeException("S3 파일 업로드 실패", e);
		}
	}

	// 파일 삭제 기능
	public void deleteFile(String fileUrl) {
		String bucketName = awsS3Properties.s3().bucket();
		String fileName = fileUrl.substring(fileUrl.indexOf("amazonaws.com/") + "amazonaws.com/".length());

		DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
			.bucket(bucketName)
			.key(fileName)
			.build();

		s3Client.deleteObject(deleteRequest);
	}

	private String getFileExtension(String originalFileName) {
		if (originalFileName == null || !originalFileName.contains(".")) {
			return "";
		}
		return originalFileName.substring(originalFileName.lastIndexOf("."));
	}
}
