package com.freela.api.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Introspected
@Schema(name="Page", description="Page information")
public class PageDto<T extends DtoInterface> {
	List<T> content = new ArrayList<>();
	Long totalSize;
	Integer pageSize;
	Integer pageNumber;
	Integer totalPages;

	public void add(@NonNull T obj) {
		content.add(obj);
	}

	public void setContent(@NonNull List<T> list) {
		content = list;
	}

	public Long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public List<T> getContent() {
		return content;
	}
}
