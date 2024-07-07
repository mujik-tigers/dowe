package com.dowe.exception;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dowe.auth.application.AuthService;
import com.dowe.exception.member.MemberRegisterException;
import com.dowe.util.api.ApiResponse;
import com.dowe.util.api.ResponseResult;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

	private final AuthService authService;

	@ExceptionHandler(BindException.class)
	public ResponseEntity<ApiResponse<Object>> handleBindException(BindException exception) {
		return ResponseEntity.badRequest()
			.body(ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseResult.EXCEPTION_OCCURRED,
					exception.getBindingResult().getFieldErrors().stream()
						.collect(Collectors.groupingBy(FieldError::getField))
						.entrySet().stream()
						.map(error -> {
							Map<String, Object> fieldError = new LinkedHashMap<>();
							fieldError.put("type", error.getKey());
							fieldError.put("message", error.getValue().stream()
								.map(DefaultMessageSourceResolvable::getDefaultMessage)
								.collect(Collectors.joining(", ")));
							return fieldError;
						})
				)
			);
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException customException) {
		Map<String, String> error = new LinkedHashMap<>(2);
		error.put("type", customException.getClass().getSimpleName());
		error.put("message", customException.getMessage());

		return ResponseEntity.status(customException.getStatus())
			.body(ApiResponse.of(customException.getStatus(), ResponseResult.EXCEPTION_OCCURRED, List.of(error)));
	}

	@ExceptionHandler(MemberRegisterException.class)
	public ResponseEntity<ApiResponse<Object>> handleMemberRegisterException(MemberRegisterException exception) {
		return ResponseEntity.ok()
			.body(ApiResponse.ok(ResponseResult.LOGIN_SUCCESS, authService.generateLoginData(exception.getProvider(), exception.getAuthId())));
	}

}
