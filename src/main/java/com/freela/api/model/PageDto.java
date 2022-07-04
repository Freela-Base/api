package com.freela.api.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

@Introspected
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
