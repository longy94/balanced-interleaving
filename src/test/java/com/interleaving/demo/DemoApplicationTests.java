package com.interleaving.demo;

import com.interleaving.demo.model.Post;
import com.interleaving.demo.model.ResponseModel;
import com.interleaving.demo.service.InterleavingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoApplicationTests {
    private final static int USER_ID = 1;
    @Value("${total.size}")
    private int SIZE;
    @Autowired
    InterleavingService interleavingService;

    @Test
    void success() {
        int perPage = 10;
        int totalPage = (SIZE + perPage - 1) / perPage;
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i <= totalPage; i++) {
            ResponseModel response = interleavingService.getPosts(USER_ID, i, perPage);
            assertEquals("200", response.getStatus());
            posts.addAll(response.getData().getResults());
        }
        int[] visited = new int[SIZE];
        posts.forEach(post -> visited[post.getId() - 1]++);
        // Make sure each post is visited once and only once
        for (int cnt : visited) {
            assertEquals(1, cnt);
        }
    }

    @Test
    void lastPageIsNotFull() {
        int perPage = 14;
        int totalPage = (SIZE + perPage - 1) / perPage;
        ResponseModel response = interleavingService.getPosts(USER_ID, totalPage, perPage);
        assertEquals("200", response.getStatus());
        assertEquals(SIZE - perPage * (totalPage - 1), response.getData().getResults().size());
    }

    @Test
    void largerThanTotalPage() {
        int perPage = 10;
        ResponseModel response = interleavingService.getPosts(USER_ID, 10, perPage);
        assertEquals("200", response.getStatus());
        assertEquals(0, response.getData().getResults().size());
    }

    @ParameterizedTest()
    @CsvSource({
            "0, 10",
            "1, 0",
            "0, 0"
    })
    void invalidRequest(int page, int perPage) {
        ResponseModel response = interleavingService.getPosts(USER_ID, page, perPage);
        assertEquals("400", response.getStatus());
        assertNull(response.getData());
    }
}