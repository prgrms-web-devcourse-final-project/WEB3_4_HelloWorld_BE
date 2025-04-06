package org.helloworld.gymmate.common.validate.custom;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public abstract class AbstractImageFileValidator {

	protected static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png", "image/gif");
	// TODO : 이미지 사이즈 제한 크기 yml로 관리
	protected static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB 제한

	protected String validateFile(MultipartFile file) {
		if (file.isEmpty())
			return "비어 있는 파일은 업로드할 수 없습니다.";
		if (!ALLOWED_TYPES.contains(file.getContentType()))
			return "지원되지 않는 이미지 형식입니다. (JPG, PNG, GIF)";
		if (file.getSize() > MAX_FILE_SIZE)
			return "이미지 파일 크기는 최대 10MB까지 가능합니다.";
		return null;
	}
}