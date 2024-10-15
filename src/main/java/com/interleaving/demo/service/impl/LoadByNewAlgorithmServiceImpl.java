package com.interleaving.demo.service.impl;

import com.interleaving.demo.model.Post;
import com.interleaving.demo.service.LoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadByNewAlgorithmServiceImpl implements LoadService {
    private final List<Post> newPosts;
    @Override
    public List<Post> load() {
        return newPosts;
    }
}
