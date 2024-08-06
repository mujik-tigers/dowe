package com.dowe.team.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TeamSettings {

	@NotEmpty
	@Size(max = 15, message = "타이틀은 최대 15자 이내로 설정해 주세요")
	private String title;

	@Size(max = 100, message = "설명은 최대 100자 이내로 설정해 주세요")
	private String description;

	private String image;

}
