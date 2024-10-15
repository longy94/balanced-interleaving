package com.interleaving.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class ResponseModel {
    private final String status;
    private final Pagination data;

    @Data
    public static class Pagination {
        private final int curPage;
        private final int totalPage;
        private final List<Post> results;
        private final String newIds;
        private final String oldIds;
    }
}
