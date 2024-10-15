package com.interleaving.demo.service;

import com.interleaving.demo.model.Post;

import java.util.List;

public interface LoadService {
    int SIZE = 30;
    List<Post> load();
}
