package org.helloworld.gymmate.common.s3;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

	private final FileManager fileManager;

	// 단일 파일 업로드
	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		String fileUrl = fileManager.uploadFile(file, "images");
		return ResponseEntity.ok(fileUrl);
	}

	// 여러 개의 파일 업로드
	@PostMapping("/upload-multiple")
	public ResponseEntity<List<String>> uploadMultipleFiles(@RequestParam("files") List<MultipartFile> files) {
		List<String> fileUrls = fileManager.uploadFiles(files, "images");
		return ResponseEntity.ok(fileUrls);
	}
}