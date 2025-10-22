package com.example.blog_app.controller;

import com.example.blog_app.entity.About;
import com.example.blog_app.service.AboutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AboutController {

    @Autowired
    private AboutService aboutService;

    @GetMapping("/about")
    public String aboutPage(Model model) {
        About about = aboutService.getAboutInfo();
        if (about == null) {
            about = new About();
            about.setAuthorName("Nguyen Quang Huy");
            about.setPosition("Java Developer");
            about.setDescription(
                    "“The Future Writer” không chỉ là một trang blog, mà là hành trình ghi lại những bước đi của tôi trên con đường trở thành một người viết – không chỉ viết bằng ngôn từ, mà còn bằng dòng code, bằng ý tưởng và bằng niềm đam mê sáng tạo. " +
                            "Tôi là Nguyễn Quang Huy – một lập trình viên Java luôn tin rằng mỗi dòng code cũng có thể kể một câu chuyện, mỗi dự án đều là một chương mới của tương lai. " +
                            "Thông qua blog này, tôi muốn chia sẻ kiến thức, kinh nghiệm và cảm hứng cho những ai đang bước trên hành trình viết nên tương lai của chính mình – như những 'future writers' của thời đại công nghệ."
            );
            about.setAvatarUrl("/images/avatar.png");
        }
        model.addAttribute("about", about);
        return "about";
    }
}
