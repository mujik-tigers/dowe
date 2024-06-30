package com.dowe.config.converter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.LinkedMultiValueMap;

import com.dowe.auth.dto.AccessTokenRequestParameter;

public class FormUrlEncodedHttpMessageConverter implements HttpMessageConverter<AccessTokenRequestParameter> {

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return false;
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return clazz.isAssignableFrom(AccessTokenRequestParameter.class) &&
			(MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType));
	}

	@Override
	public List<MediaType> getSupportedMediaTypes() {
		return Collections.singletonList(MediaType.APPLICATION_FORM_URLENCODED);
	}

	@Override
	public AccessTokenRequestParameter read(Class<? extends AccessTokenRequestParameter> clazz, HttpInputMessage inputMessage) throws
		IOException,
		HttpMessageNotReadableException {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public void write(AccessTokenRequestParameter accessTokenRequestParameter, MediaType contentType, HttpOutputMessage outputMessage) throws
		IOException,
		HttpMessageNotWritableException {
		LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		for (Field field : accessTokenRequestParameter.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				params.add(field.getName(), field.get(accessTokenRequestParameter).toString());
			} catch (IllegalAccessException e) {
				throw new IOException("Failed to access field", e);
			}
		}

		StringBuilder body = new StringBuilder();
		for (String key : params.keySet()) {
			for (String value : params.get(key)) {
				if (body.length() > 0) {
					body.append("&");
				}
				body.append(key).append("=").append(value);
			}
		}

		OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), StandardCharsets.UTF_8);
		writer.write(body.toString());
		writer.flush();
	}
}
