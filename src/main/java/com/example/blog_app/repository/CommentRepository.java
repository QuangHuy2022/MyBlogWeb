package com.example.blog_app.repository;

import com.example.blog_app.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Tìm tất cả comment theo Post ID
    List<Comment> findByPostId(Long postId);
}
