package com.dowe.elasticsearch.mapper;

import com.dowe.team.Team;
import com.dowe.team.dto.TeamOutline;
import com.dowe.util.AppConstants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = AppConstants.class)
public interface TeamMapper {

  @Mapping(target = "maxPeople", expression = "java(AppConstants.TEAM_MAX_SIZE)")
  TeamOutline toTeamOutline(Team team);

}
