package com.dowe.team.dto;

public record TeamOutline(
    Long id,
    String title,
    String description,
    String image,
    int currentPeople,
    int maxPeople
) {

}
