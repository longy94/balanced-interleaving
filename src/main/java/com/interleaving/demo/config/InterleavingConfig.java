package com.interleaving.demo.config;

import com.interleaving.demo.model.Post;
import com.interleaving.demo.model.SourceConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class InterleavingConfig {
    @Value("${total.size}")
    private int SIZE;
    @Bean
    public List<Post> oldPosts() {
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) {
            posts.add(new Post(i, SourceConstants.OLD));
        }
        return posts;
    }

    @Bean
    public List<Post> newPosts() {
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) {
            posts.add(new Post(i, SourceConstants.NEW));
        }
        Collections.shuffle(posts);
        return posts;
    }
}
