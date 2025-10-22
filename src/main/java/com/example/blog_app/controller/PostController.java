package com.example.blog_app.controller;

import com.example.blog_app.entity.Category;
import com.example.blog_app.entity.Post;
import com.example.blog_app.entity.Comment;
import com.example.blog_app.service.PostService;
import com.example.blog_app.service.CommentService;
import com.example.blog_app.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private com.example.blog_app.repository.PostRepository postRepository;


    // -------------------------------
    // 📍 TRANG CHỦ
    // -------------------------------
    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 5; // số bài trên mỗi trang
        var postPage = postService.getPaginatedPosts(page, pageSize);

        // Lấy danh sách bài viết phân trang
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());

        // ✅ Thêm danh sách 6 bài viết mới nhất (cho phần "Bài viết mới nhất")
        List<Post> latestPosts = postService.getLatestPosts(6);
        model.addAttribute("latestPosts", latestPosts);

        return "home"; // -> resources/templates/home.html
    }


    // -------------------------------
    // 📍 TRANG BLOG (hiển thị toàn bộ bài viết)
    // -------------------------------
    // -------------------------------
// 📍 TRANG BLOG (hiển thị toàn bộ bài viết có phân trang)
// -------------------------------
    @GetMapping("/blog")
    public String blog(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 3; // mỗi trang 3 bài
        var postPage = postService.getPaginatedPosts(page, pageSize);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());

        // ✅ Thêm danh sách category cho sidebar
        model.addAttribute("categories", categoryService.getAllCategories());

        // ✅ Thêm bài viết mới nhất cho sidebar
        List<Post> latestPosts = postService.getLatestPosts(5);
        model.addAttribute("latestPosts", latestPosts);
        for (Post p : postPage.getContent()) {
            String plainText = p.getContent().replaceAll("<[^>]*>", ""); // bỏ hết tag HTML
            if (plainText.length() > 150) {
                plainText = plainText.substring(0, 150) + "...";
            }
            p.setContent(plainText);
        }

        return "blog"; // -> resources/templates/blog.html
    }

    // -------------------------------
    // 📍 XEM CHI TIẾT BÀI VIẾT
    // -------------------------------
    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = postService.getById(id);

        if (post == null) {
            return "redirect:/"; // Nếu bài viết không tồn tại
        }

        model.addAttribute("post", post);
        model.addAttribute("comments", commentService.getCommentsByPost(id));
        model.addAttribute("comment", new Comment());
        model.addAttribute("relatedPosts", postService.getLatestPosts(3)); // Gợi ý thêm vài bài khác
        return "post-detail";
    }

    // -------------------------------
    // 📍 FORM TẠO BÀI VIẾT MỚI
    // -------------------------------
    @GetMapping("/post/new")
    public String newPostForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "new-post";
    }

    // -------------------------------
    // 📍 LƯU BÀI VIẾT MỚI
    // -------------------------------
    @PostMapping("/post/save")
    public String savePost(@ModelAttribute Post post,
                           @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        // Xử lý link YouTube (chuyển sang dạng nhúng)
        if (post.getVideoUrl() != null && post.getVideoUrl().contains("watch?v=")) {
            post.setVideoUrl(post.getVideoUrl().replace("watch?v=", "embed/"));
        }

        // Upload file video nếu có
        if (file != null && !file.isEmpty()) {
            String uploadDir = "src/main/resources/static/uploads/";
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            post.setVideoFile("/uploads/" + fileName);
        }

        postService.save(post);
        return "redirect:/";
    }

    // -------------------------------
    // 📍 FORM CHỈNH SỬA BÀI VIẾT
    // -------------------------------
    @GetMapping("/post/edit/{id}")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.getById(id);
        if (post == null) return "redirect:/";
        model.addAttribute("post", post);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit-post";
    }

    // -------------------------------
    // 📍 CẬP NHẬT BÀI VIẾT
    // -------------------------------
    @PostMapping("/post/update/{id}")
    public String updatePost(@PathVariable Long id,
                             @ModelAttribute Post post,
                             @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Post existing = postService.getById(id);

        if (existing != null) {
            existing.setTitle(post.getTitle());
            existing.setContent(post.getContent());
            existing.setCategory(post.getCategory());
            existing.setImageUrl(post.getImageUrl());

            // Cập nhật YouTube URL nếu có
            if (post.getVideoUrl() != null && post.getVideoUrl().contains("watch?v=")) {
                existing.setVideoUrl(post.getVideoUrl().replace("watch?v=", "embed/"));
            } else {
                existing.setVideoUrl(post.getVideoUrl());
            }

            // Upload video mới nếu có
            if (file != null && !file.isEmpty()) {
                String uploadDir = "src/main/resources/static/uploads/";
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get(uploadDir + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());
                existing.setVideoFile("/uploads/" + fileName);
            }

            postService.save(existing);
        }

        return "redirect:/post/" + id;
    }

    // -------------------------------
    // 📍 XÓA BÀI VIẾT
    // -------------------------------
    @GetMapping("/post/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.delete(id);
        return "redirect:/";
    }
    @GetMapping("/blog/search")
    public String searchPosts(@RequestParam("keyword") String keyword,
                              @RequestParam(defaultValue = "1") int page,
                              Model model) {

        int pageSize = 3; // Giống blog thường
        var postPage = postService.searchByTitle(keyword, page, pageSize);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        // ✅ Bổ sung dữ liệu cho sidebar
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("latestPosts", postService.getLatestPosts(5));

        return "blog";
    }
    @GetMapping("/blog/category/{id}")
    public String getPostsByCategory(@PathVariable Long id, Model model) {
        List<Post> posts = postService.getPostsByCategory(id);
        List<Category> categories = categoryService.getAllCategories();

        List<Post> latestPosts = postService.getLatestPosts(3);

        model.addAttribute("posts", posts);
        model.addAttribute("categories", categories);
        model.addAttribute("latestPosts", latestPosts);
        model.addAttribute("selectedCategoryId", id);

        return "blog"; // tên template của bạn (ví dụ: blog.html hoặc home.html)
    }


}
