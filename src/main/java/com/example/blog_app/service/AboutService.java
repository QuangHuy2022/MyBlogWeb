package com.example.blog_app.service;

import com.example.blog_app.entity.About;
import com.example.blog_app.repository.AboutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AboutService {

    @Autowired
    private AboutRepository aboutRepository;

    public About getAboutInfo() {
        return aboutRepository.findAll().stream().findFirst().orElse(null);
    }

    public void save(About about) {
        aboutRepository.save(about);
    }
}
