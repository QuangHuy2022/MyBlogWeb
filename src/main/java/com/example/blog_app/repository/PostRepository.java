package com.example.blog_app.repository;

import com.example.blog_app.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // ✅ Lấy danh sách bài viết mới nhất, giới hạn theo số lượng truyền vào
    @Query(value = "SELECT * FROM posts ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
    List<Post> findTopByOrderByCreatedAtDesc(@Param("limit") int limit);
    List<Post> findByTitleContainingIgnoreCase(String keyword);
    Page<Post> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    List<Post> findByCategoryId(Long categoryId);

}
