package com.freela.service.parameter;

import io.micronaut.core.annotation.Introspected;

//TODO Bring those request parameters to service folder (for page request, replace individual parameter calls with object)
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
