package com.siukatech.poc.react.backend.module.core.data.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class PageModel<T> extends org.springframework.data.domain.PageImpl<T> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PageModel(@JsonProperty("content") List<T> content, @JsonProperty("number") int number,
                     @JsonProperty("size") int size, @JsonProperty("totalElements") Long totalElements,
                     @JsonProperty("pageable") JsonNode pageable, @JsonProperty("last") boolean last,
                     @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort,
                     @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, PageRequest.of(number, 1), 10);
    }

    public PageModel(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageModel(List<T> content) {
        // Prevent java.lang.UnsupportedOperationException from org.springframework.data.domain.Unpaged.getOffset
//        super(content);
        super(content, PageRequest.of(0, content.size()), content.size());
    }

    public PageModel() {
        super(new ArrayList<>());
    }
}
