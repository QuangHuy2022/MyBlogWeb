package com.example.blog_app.controller;

import com.example.blog_app.entity.Contact;
import com.example.blog_app.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contact")
    public String contactForm(Model model) {
        model.addAttribute("contact", new Contact());
        return "contact";
    }

    @PostMapping("/contact/save")
    public String saveContact(@ModelAttribute Contact contact, Model model) {
        contactService.save(contact);
        model.addAttribute("success", "Cảm ơn bạn đã liên hệ! Mình sẽ phản hồi sớm nhất có thể.");
        return "contact";
    }
}
