package com.leave.mapper;

import com.leave.dto.LeaveDTO;
import com.leave.model.Leave;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LeaveMapper {
    LeaveMapper INSTANCE = Mappers.getMapper(LeaveMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Leave toEntity(LeaveDTO dto);

    LeaveDTO toDTO(Leave entity);
} 