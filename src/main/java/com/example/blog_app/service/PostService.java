package com.example.blog_app.service;

import com.example.blog_app.entity.Post;
import com.example.blog_app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public Post getById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }
    public List<Post> getLatestPosts(int limit) {
        return postRepository.findTopByOrderByCreatedAtDesc(limit);
    }


    // ✅ Phân trang
    public Page<Post> getPaginatedPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postRepository.findAll(pageable);
    }
    public Page<Post> searchByTitle(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }
    public List<Post> getPostsByCategory(Long categoryId) {
        return postRepository.findByCategoryId(categoryId);
    }



}
