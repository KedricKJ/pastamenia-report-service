package com.pastamenia.dto.wrapper;

import com.pastamenia.enums.RestApiResponseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 
 * @author Nuwan
 *
 */
@Getter
@Setter
@EqualsAndHashCode
public class PagingListResponseWrapper<T> extends BaseResponseWrapper {

	private List<T> content;

	private Pagination pagination;

	public PagingListResponseWrapper(List<T> content, Pagination pagination) {
		super(RestApiResponseStatus.OK);
		this.content = content;
		this.pagination = pagination;
	}

	@Data
	public static class Pagination {

		public Pagination(Integer pageNumber, Integer pageSize, Integer totalPages, Long totalRecords) {
			this.pageNumber = pageNumber;
			this.pageSize = pageSize;
			this.totalPages = totalPages;
			this.totalRecords = totalRecords;
		}

		private Integer pageNumber;

		private Integer pageSize;

		private Integer totalPages;

		private Long totalRecords;

	}
}
