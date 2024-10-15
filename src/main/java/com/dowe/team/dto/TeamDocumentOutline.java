package com.dowe.team.dto;

public record TeamDocumentOutline(
    Long id,
    String title,
    String description,
    String image,
    int currentPeople,
    int maxPeople
) {

}
