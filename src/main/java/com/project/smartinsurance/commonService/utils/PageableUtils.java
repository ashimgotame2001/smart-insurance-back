package com.project.smartinsurance.commonService.utils;

import com.project.smartinsurance.commonService.dto.PageFilterRequest;
import com.project.smartinsurance.commonService.dto.PagedData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

/**
 * Builds Spring {@link Pageable} from {@link PageFilterRequest} and maps {@link Page} to {@link PagedData}.
 */
public final class PageableUtils {

    private PageableUtils() {
    }

    public static Pageable toPageable(PageFilterRequest request) {
        if (request == null) {
            return PageRequest.of(
                    PageFilterRequest.DEFAULT_PAGE,
                    PageFilterRequest.DEFAULT_SIZE,
                    Sort.by(PageFilterRequest.DEFAULT_SORT_BY).descending());
        }

        int page = Math.max(request.getPage(), 0);
        int size = request.getSize() <= 0
                ? PageFilterRequest.DEFAULT_SIZE
                : Math.min(request.getSize(), PageFilterRequest.MAX_SIZE);
        String sortBy = StringUtils.hasText(request.getSortBy())
                ? request.getSortBy()
                : PageFilterRequest.DEFAULT_SORT_BY;
        boolean ascending = request.getSortDir() != null
                && request.getSortDir().equalsIgnoreCase("asc");

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }

    public static <T> PagedData<T> toPagedData(Page<T> page) {
        return PagedData.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
