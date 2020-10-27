package com.java.springboot.task.mapper;

import com.java.springboot.task.dto.ComicDto;
import com.java.springboot.task.entity.Comic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.stream.Collectors;

@Mapper
public interface ComicsMapper {

    ComicsMapper MAPPER = Mappers.getMapper(ComicsMapper.class);

    @Mappings({
            @Mapping(source = "comic.id", target = "id"),
            @Mapping(source = "comic.name", target = "name"),
    })
    ComicDto mapToDto(Comic comic);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
    })
    Comic mapToEntiy(ComicDto comic);

    default Page<ComicDto> mapCollectionToDto(
            Page<Comic> source) {
        return new PageImpl(source.getContent().stream().map(this::mapToDto).collect(Collectors.toList()), source.getPageable(), source.getTotalElements());
    }
}
