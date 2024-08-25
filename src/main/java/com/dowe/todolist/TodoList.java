package com.dowe.todolist;

import com.dowe.profile.Profile;
import com.dowe.util.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoList extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "profile_id")
	private Profile profile;

	private boolean completed;

	@Builder
	private TodoList(
		Profile profile
	) {
		this.profile = profile;
		this.completed = false;
	}

	public static TodoList of(
		Profile profile
	) {
		return TodoList.builder()
			.profile(profile)
			.build();
	}
}
