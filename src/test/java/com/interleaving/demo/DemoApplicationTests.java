package com.interleaving.demo;

import com.interleaving.demo.model.Post;
import com.interleaving.demo.model.ResponseModel;
import com.interleaving.demo.model.SourceConstants;
import com.interleaving.demo.service.InterleavingService;
import com.interleaving.demo.service.LoadService;
import com.interleaving.demo.service.impl.BalancedInterleavingServiceImpl;
import com.interleaving.demo.service.impl.LoadByNewAlgorithmServiceImpl;
import com.interleaving.demo.service.impl.LoadByOldAlgorithmServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class DemoApplicationTests {
    private final static int USER_ID = 1;
    @Value("${total.size}")
    private int SIZE;
    @Autowired
    List<Post> oldPosts;
    @Autowired
    InterleavingService interleavingService;
    // custom your new posts
    private List<Post> createNewPosts() {
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) {
            posts.add(new Post(i, SourceConstants.NEW));
        }
        Collections.shuffle(posts);
        return posts;
    }

    private void checkMissingOrDuplicate(List<Post> posts) {
        int[] visited = new int[SIZE];
        posts.forEach(post -> visited[post.getId() - 1]++);
        // Make sure each post is visited once and only once
        for (int cnt : visited) {
            assertEquals(1, cnt);
        }
    }
    @Test
    void custom() {
        int perPage = 10;
        int totalPage = (SIZE + perPage - 1) / perPage;
        LoadService loadNewService = mock(LoadByNewAlgorithmServiceImpl.class);
        InterleavingService mockInterleavingService = new BalancedInterleavingServiceImpl(loadNewService, new LoadByOldAlgorithmServiceImpl(oldPosts));
        when(loadNewService.load()).thenReturn(createNewPosts());
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i <= totalPage; i++) {
            ResponseModel response = mockInterleavingService.getPosts(USER_ID, i, perPage);
            assertEquals("200", response.getStatus());
            assertTrue(response.getData().getResults().size() <= perPage);
            posts.addAll(response.getData().getResults());
        }
        checkMissingOrDuplicate(posts);
    }

    @Test
    void success() {
        int perPage = 10;
        int totalPage = (SIZE + perPage - 1) / perPage;
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i <= totalPage; i++) {
            ResponseModel response = interleavingService.getPosts(USER_ID, i, perPage);
            assertEquals("200", response.getStatus());
            assertTrue(response.getData().getResults().size() <= perPage);
            posts.addAll(response.getData().getResults());
        }
        checkMissingOrDuplicate(posts);
    }

    @Test
    void lastPageIsNotFull() {
        int perPage = 14;
        int totalPage = (SIZE + perPage - 1) / perPage;
        ResponseModel response = interleavingService.getPosts(USER_ID, totalPage, perPage);
        assertEquals("200", response.getStatus());
        assertTrue(response.getData().getResults().size() <= perPage);
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