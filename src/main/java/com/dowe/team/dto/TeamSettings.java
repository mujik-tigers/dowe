package com.dowe.team.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamSettings {

	@NotEmpty(message = "타이틀은 최소 1자 이상으로 설정해 주세요")
	@Size(max = 15, message = "타이틀은 최대 15자 이내로 설정해 주세요")
	private String title;

	@Size(max = 100, message = "설명은 최대 100자 이내로 설정해 주세요")
	private String description;

	private MultipartFile image;

}
