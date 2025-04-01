package org.helloworld.gymmate.domain.user.trainer.business.service;

import java.net.URI;
import java.util.Map;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.user.trainer.business.dto.request.BusinessVerificationRequest;
import org.helloworld.gymmate.domain.user.trainer.business.dto.response.BusinessVerificationResponse;
import org.helloworld.gymmate.domain.user.trainer.business.properties.BusinessProperties;
import org.helloworld.gymmate.domain.user.trainer.dto.OwnerRegisterRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessValidateService {
	private final RestTemplate restTemplate;
	private final BusinessProperties businessProperties;

	public void validateBusiness(OwnerRegisterRequest ownerRegisterRequest) {
		String validateUrl = businessProperties.getUrl() + "?serviceKey=" + businessProperties.getServiceKey();
		URI uri = URI.create(validateUrl);

		BusinessVerificationRequest businessVerificationRequest = BusinessVerificationRequest.from(
			ownerRegisterRequest);

		// HTTP 헤더 설정 (JSON 전송)
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<BusinessVerificationRequest> requestEntity =
			new HttpEntity<>(businessVerificationRequest, headers);

		BusinessVerificationResponse response = restTemplate.postForObject(uri,
			requestEntity, BusinessVerificationResponse.class);

		if (response == null || response.data() == null || response.data().isEmpty()) {
			throw new BusinessException(ErrorCode.API_UNEXPECTED_RESPONSE);
		}

		Map<String, Object> result = response.data().getFirst();
		String valid = (String)result.get("valid");

		if ("01".equals(valid)) {
			log.debug("사업자등록번호 인증 성공");
		} else {
			throw new BusinessException(ErrorCode.INVALID_BUSINESS_NUMBER);
		}
	}
}
