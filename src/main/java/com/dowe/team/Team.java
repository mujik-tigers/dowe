package com.dowe.team;

import com.dowe.util.BaseEntity;
import jakarta.persistence.CascadeType;

import com.dowe.profile.Profile;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;
  private String image;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "manager_profile_id")
  private Profile managerProfile;

  @Builder
  public Team(
      String title,
      String description,
      String image
  ) {
    this.title = title;
    this.description = description;
    this.image = image;
  }

  public void assignManagerProfile(Profile profile) {
    this.managerProfile = profile;
  }

}
