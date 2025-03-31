package org.helloworld.gymmate.common.rq;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequestScope
@Component
@RequiredArgsConstructor
public class Rq {
	private final HttpServletRequest request;

	public String getParameter(String name) {
		return request.getParameter(name);
	}
}
