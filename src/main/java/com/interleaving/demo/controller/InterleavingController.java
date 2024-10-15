package com.interleaving.demo.controller;

import com.interleaving.demo.model.ResponseModel;
import com.interleaving.demo.service.InterleavingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/interleaving")
@RequiredArgsConstructor
public class InterleavingController {
    private final InterleavingService interleavingService;
    @GetMapping("/{user_id}")
    public ResponseModel get(@PathVariable("user_id") int userId, @RequestParam("page") int page, @RequestParam("per_page") int perPage) {
        return interleavingService.getPosts(userId, page, perPage);
    }
}
