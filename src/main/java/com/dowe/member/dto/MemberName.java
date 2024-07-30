package com.dowe.member.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberName {

	@NotEmpty
	@Size(max = 20, message = "이름은 최대 20자 이내로 설정해 주세요")
	private String newName;

}
