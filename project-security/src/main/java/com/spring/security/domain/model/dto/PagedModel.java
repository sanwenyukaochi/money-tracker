package com.spring.security.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import java.util.List;

public record PagedModel<T>(Page<@NonNull T> page) {

    public PagedModel(Page<@NonNull T> page) {
        Assert.notNull(page, "Page must not be null");
        this.page = page;
    }

    @JsonProperty
    public List<T> getContent() {
        return page.getContent();
    }

    @JsonProperty("page")
    public PageMetadata getMetadata() {
        return new PageMetadata(page.getSize(), page.getNumber() + 1, page.getTotalElements(),
                page.getTotalPages());
    }

    public record PageMetadata(long size, long number, long totalElements, long totalPages) {

        public PageMetadata {
            Assert.isTrue(size > -1, "Size must not be negative!");
            Assert.isTrue(number > -1, "Number must not be negative!");
            Assert.isTrue(totalElements > -1, "Total elements must not be negative!");
            Assert.isTrue(totalPages > -1, "Total pages must not be negative!");
        }
    }

    public static <T> PagedModel<T> of(Page<@NonNull T> page) {
        Assert.notNull(page, "Page must not be null");
        return new PagedModel<>(page);
    }
}
