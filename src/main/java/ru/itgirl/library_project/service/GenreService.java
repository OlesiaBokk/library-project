package ru.itgirl.library_project.service;

import ru.itgirl.library_project.dto.GenreDto;

public interface GenreService {
    GenreDto getGenreById(Long id);
}
