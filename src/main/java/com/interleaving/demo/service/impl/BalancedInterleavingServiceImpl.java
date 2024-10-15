package com.interleaving.demo.service.impl;

import com.interleaving.demo.model.Post;
import com.interleaving.demo.model.ResponseModel;
import com.interleaving.demo.service.InterleavingService;
import com.interleaving.demo.service.LoadService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

@Service
public class BalancedInterleavingServiceImpl implements InterleavingService {
    private final LoadService loadNewService;
    private final LoadService loadOldService;

    public BalancedInterleavingServiceImpl(@Qualifier("loadByNewAlgorithmServiceImpl") LoadService loadNewService, @Qualifier("loadByOldAlgorithmServiceImpl") LoadService loadOldService) {
        this.loadNewService = loadNewService;
        this.loadOldService = loadOldService;
    }

    @Override
    public ResponseModel getPosts(int userId, int page, int perPage) {
        try {
            validate(page, perPage);
        } catch (IllegalArgumentException e) {
            return new ResponseModel("400", null);
        }
        List<Post> newPosts = loadNewService.load();
        List<Post> oldPosts = loadOldService.load();
        Set<Integer> set = new HashSet<>();
        List<Post> posts = new ArrayList<>();
        int indexOld = 0, indexNew = 0;
        int cnt = 0;
        int size = oldPosts.size();
        int start = (page - 1) * perPage;
        // Always take post from new algorithm
        while (posts.size() < perPage && indexOld < size && indexNew < size) {
            if (indexOld < indexNew) {
                Post post = oldPosts.get(indexOld);
                if (!set.contains(post.getId())) {
                    cnt++;
                    set.add(post.getId());
                    if (cnt > start) {
                        posts.add(post);
                    }
                }
                indexOld++;
            } else {
                Post post = newPosts.get(indexNew);
                if (!set.contains(post.getId())) {
                    cnt++;
                    set.add(post.getId());
                    if (cnt > start) {
                        posts.add(post);
                    }
                }
                indexNew++;
            }
        }
        List<Integer> oldIds = oldPosts.stream().map(Post::getId).toList();
        List<Integer> newIds = newPosts.stream().map(Post::getId).toList();
        ResponseModel.Pagination pagination = new ResponseModel.Pagination(page, (size + perPage - 1) / perPage, posts, newIds.toString(), oldIds.toString());
        return new ResponseModel("200", pagination);
    }

    private void validate(int page, int perPage) {
        if (page < 1 || perPage < 1) {
            throw new IllegalArgumentException();
        }
    }
}
