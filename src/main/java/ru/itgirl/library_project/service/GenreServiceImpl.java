package ru.itgirl.library_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itgirl.library_project.dto.GenreAuthorDto;
import ru.itgirl.library_project.dto.GenreBookDto;
import ru.itgirl.library_project.dto.GenreDto;
import ru.itgirl.library_project.model.entity.Genre;
import ru.itgirl.library_project.repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public GenreDto getGenreById(Long id) {
        Genre genre = genreRepository.findById(id).orElseThrow();
        return convertToDto(genre);
    }

    @Override
    public GenreDto getGenreByName(String name) {
        Genre genre = genreRepository.findGenreByName(name).orElseThrow();
        return convertToDto(genre);
    }

    private GenreDto convertToDto(Genre genre) {
        List<GenreBookDto> genreBookDtoList = genre.getBooks()
                .stream()
                .map(book -> GenreBookDto.builder()
                        .id(book.getId())
                        .name(book.getName())
                        .authors(book.getAuthors()
                                .stream()
                                .map(author -> GenreAuthorDto.builder()
                                        .id(author.getId())
                                        .name(author.getName())
                                        .surname(author.getSurname())
                                        .build()).toList())
                        .build()).toList();
        return GenreDto.builder()
                .id(genre.getId())
                .genre(genre.getName())
                .books(genreBookDtoList).build();
    }
}
