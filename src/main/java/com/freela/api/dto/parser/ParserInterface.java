package com.freela.api.dto.parser;

import com.freela.api.dto.DtoInterface;
import com.freela.api.dto.PageDto;
import com.freela.database.model.ModelInterface;
import io.micronaut.data.model.Page;

import java.util.List;
import java.util.Objects;

public interface ParserInterface<MODEL extends ModelInterface, DTO extends DtoInterface> {
	DTO dtoParser(MODEL model);
	MODEL modelParser(DTO dto);

	default DTO toDto(MODEL model) {
		if (model == null) {
			return null;
		}

		return dtoParser(model);
	}

	default MODEL toModel(DTO dto) {
		if (dto == null) {
			return null;
		}

		return modelParser(dto);
	}

	default PageDto<DTO> toPagedDto(Page<MODEL> pagedModels) {
		if (pagedModels == null) {
			return null;
		}

		PageDto<DTO> pagedDtos = new PageDto<>();
		pagedDtos.setTotalSize(pagedModels.getTotalSize());
		pagedDtos.setTotalPages(pagedModels.getTotalPages());
		pagedDtos.setPageNumber(pagedModels.getPageNumber());
		pagedDtos.setPageSize(pagedModels.getNumberOfElements());

		List<DTO> dtos = pagedModels.getContent().stream()
				.map(this::toDto)
				.filter(Objects::nonNull).toList();
		pagedDtos.setContent(dtos);

		return pagedDtos;
	}
}
