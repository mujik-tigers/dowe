package com.dowe.elasticsearch.mapper;

import com.dowe.elasticsearch.document.TeamDocument;
import com.dowe.team.dto.TeamDocumentOutline;
import com.dowe.util.AppConstants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = AppConstants.class)
public interface TeamDocumentMapper {

  @Mapping(target = "maxPeople", expression = "java(AppConstants.TEAM_MAX_SIZE)")
  TeamDocumentOutline toTeamDocumentOutline(TeamDocument teamDocument);

}
