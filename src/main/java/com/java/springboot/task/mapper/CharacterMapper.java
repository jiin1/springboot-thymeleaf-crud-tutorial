package com.java.springboot.task.mapper;

import com.java.springboot.task.dto.CharacterDto;
import com.java.springboot.task.entity.Character;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper( uses = ComicsMapper.class )
public interface CharacterMapper {
    CharacterMapper MAPPER = Mappers.getMapper(CharacterMapper.class);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "nameStartsWith", source = "nameStartsWith"),
            @Mapping(target = "age", source = "age"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "comic", source = "comic")
    })
    CharacterDto mapToDto(Character source);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "nameStartsWith", source = "nameStartsWith"),
            @Mapping(target = "age", source = "age"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "comic", source = "comic")
    })
    Character mapToEntity(CharacterDto source);

    default Page<CharacterDto> mapCollectionToDto( Page<Character> source) {
        return new PageImpl(source.getContent().stream().map(this::mapToDto).collect(Collectors.toList()), source.getPageable(), source.getTotalElements());
    }

   default List<CharacterDto> mapCollectionToDto( List<Character> source) {
       List<CharacterDto> list = new ArrayList<CharacterDto>();
       list.addAll(source.stream().map(this::mapToDto).collect(Collectors.toList()));
       return list;
   }

}