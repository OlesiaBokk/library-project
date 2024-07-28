package ru.itgirl.library_project.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itgirl.library_project.service.AuthorService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorViewController {
    private final AuthorService authorService;

    @GetMapping("/authors")
    String getAuthorsView(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        return "authors";
    }
}
