package com.dowe.todolist;

public class TodoList extends BaseEntity {

	private Long id;

	private Profile profile;

	private boolean completed;

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
