package com.dowe.config.converter;

import org.springframework.core.convert.converter.Converter;

import com.dowe.member.Provider;

public class ProviderConverter implements Converter<String, Provider> {

	@Override
	public Provider convert(String type) {
		return Provider.valueOf(type.toUpperCase());
	}

}
