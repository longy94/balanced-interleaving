package com.interleaving.demo.service;

import com.interleaving.demo.model.ResponseModel;

public interface InterleavingService {
    ResponseModel getPosts(int userId, int page, int perPage);
}
