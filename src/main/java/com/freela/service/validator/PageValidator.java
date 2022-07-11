package com.freela.service.validator;

import com.freela.exception.ApiException;
import com.freela.exception.InvalidParameterException;
import com.freela.service.parameter.PageRequest;
import jakarta.inject.Singleton;

@Singleton
public class PageValidator {
	public static int DEFAULT_PAGE_SIZE = 100;
	public static int MAX_PAGE_SIZE = 1000;
	public static int DEFAULT_PAGE_NUMBER = 0;

	public int validatePageNumber(Integer pageNumber) {
		if(pageNumber == null) {
			return DEFAULT_PAGE_NUMBER;
		}

		if(pageNumber < 0) {
			throw new InvalidParameterException("Invalid page number", new ApiException.Source(
					ApiException.Location.QUERY,
					"pageNumber",
					String.format("%s", pageNumber),
					"Valid page number"
			));
		}

		return pageNumber;
	}

	public int validatePageSize(Integer pageSize) {
		if(pageSize == null) {
			return DEFAULT_PAGE_SIZE;
		}

		if(pageSize <= 0) {
			throw new InvalidParameterException("Invalid page size", new ApiException.Source(
					ApiException.Location.QUERY,
					"pageSize",
					String.format("%s", pageSize),
					"Page size must be grater than 0"
			));
		}

		if(pageSize > MAX_PAGE_SIZE) {
			throw new InvalidParameterException("Invalid page size", new ApiException.Source(
					ApiException.Location.QUERY,
					"pageSize",
					String.format("%s", pageSize),
					String.format("Page should not be higher than %s", MAX_PAGE_SIZE)
			));
		}

		return pageSize;
	}

	public void validate(PageRequest pageRequest) {
		pageRequest.setPageNumber(validatePageNumber(pageRequest.getPageNumber()));
		pageRequest.setPageSize(validatePageSize(pageRequest.getPageSize()));
	}
}
