package com.interleaving.demo.service.impl;

import com.interleaving.demo.model.Post;
import com.interleaving.demo.service.LoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadByOldAlgorithmServiceImpl implements LoadService {
    private final List<Post> oldPosts;
    @Override
    public List<Post> load() {
        return oldPosts;
    }
}
