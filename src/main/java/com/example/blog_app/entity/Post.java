package com.example.blog_app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    // SỬA ĐỔI TẠI ĐÂY: Thêm annotation @Column(length = 1024)
    @Column(length = 1024)
    private String imageUrl;   // link ảnh (Mở rộng giới hạn từ 255 lên 1024 ký tự)

    // Tương tự, bạn có thể áp dụng cho videoUrl nếu cần (vì nó cũng là URL)
    @Column(length = 1024)
    private String videoUrl;   // link video youtube

    @Column(length = 512)
    private String videoFile;  // đường dẫn video upload (optional - có thể ngắn hơn)

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getter & Setter cho id
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // Getter & Setter cho title
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    // Getter & Setter cho content
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    // Getter & Setter cho imageUrl
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // Getter & Setter cho videoUrl
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    // Getter & Setter cho videoFile
    public String getVideoFile() { return videoFile; }
    public void setVideoFile(String videoFile) { this.videoFile = videoFile; }

    // Getter & Setter cho author
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    // Getter & Setter cho category
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    // Getter & Setter cho createdAt
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
