package com.ecommerce.site.admin.util;

/**
 * @author Nguyen Thanh Phuong
 */
public class PagingAndSortingUtil {

    private int totalPages;

    private long totalElements;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

}
