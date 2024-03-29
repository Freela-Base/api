package com.freela.service.parameter;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class PageRequest {
	Integer pageSize;
	Integer pageNumber;

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

	@Override
	public String toString() {
		return "PageRequest{" +
				"pageSize=" + pageSize +
				", pageNumber=" + pageNumber +
				'}';
	}
}
