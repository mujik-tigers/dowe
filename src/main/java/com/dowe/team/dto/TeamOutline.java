package com.dowe.team.dto;

public record TeamOutline(
    Long id,
    String title,
    String image,
    int currentPeople,
    int maxPeople
) {

}
