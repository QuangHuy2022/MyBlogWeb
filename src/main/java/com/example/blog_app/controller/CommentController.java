package com.example.blog_app.controller;

import com.example.blog_app.entity.Comment;
import com.example.blog_app.entity.Post;
import com.example.blog_app.entity.User;
import com.example.blog_app.service.CommentService;
import com.example.blog_app.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @PostMapping("/save/{postId}")
    public String saveComment(@PathVariable Long postId, @ModelAttribute Comment comment, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // bắt đăng nhập trước
        }

        Post post = postService.getById(postId);
        comment.setPost(post);
        comment.setUser(user);
        commentService.save(comment);

        return "redirect:/post/" + postId;
    }

    @GetMapping("/delete/{id}/{postId}")
    public String deleteComment(@PathVariable Long id, @PathVariable Long postId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        commentService.delete(id);
        return "redirect:/post/" + postId;
    }
}
