package com.example.blog_app.controller;

import com.example.blog_app.service.PostService;
import com.example.blog_app.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private PostService postService;

    @Autowired
    private ContactService contactService;

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("postCount", postService.getAllPosts().size());
        model.addAttribute("contactCount", contactService.getAllContacts().size());
        model.addAttribute("latestPosts", postService.getLatestPosts(5));
        model.addAttribute("contacts", contactService.getAllContacts());
        // ✅ Thêm danh sách tất cả bài viết để hiển thị CRUD
        model.addAttribute("posts", postService.getAllPosts());
        return "admin-dashboard";
    }
}
