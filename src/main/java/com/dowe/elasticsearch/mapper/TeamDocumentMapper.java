package com.dowe.elasticsearch.mapper;

import com.dowe.elasticsearch.document.TeamDocument;
import com.dowe.team.dto.TeamDocumentOutline;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeamDocumentMapper {

  TeamDocumentMapper INSTANCE = Mappers.getMapper(TeamDocumentMapper.class);

  TeamDocumentOutline toTeamDocumentOutline(TeamDocument teamDocument);

}
