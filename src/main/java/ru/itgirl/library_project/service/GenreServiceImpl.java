package ru.itgirl.library_project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.dto.GenreAuthorDto;
import ru.itgirl.library_project.dto.GenreBookDto;
import ru.itgirl.library_project.dto.GenreDto;
import ru.itgirl.library_project.model.entity.Genre;
import ru.itgirl.library_project.repository.GenreRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public GenreDto getGenreById(Long id) {
        log.info("Try to find genre by id {}", id);
        Optional<Genre> genre = genreRepository.findById(id);
        if(genre.isPresent()){
            GenreDto genreDto = convertEntityToDto(genre.get());
            log.info("Genre: {}",genreDto.toString());
            return genreDto;
        } else {
            log.error("Genre with id {} not found", id);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public GenreDto getGenreByName(String name) {
        log.info("Try to find genre by name {}", name);
        Optional<Genre> genre = genreRepository.findByName(name);
        if(genre.isPresent()){
            GenreDto genreDto = convertEntityToDto(genre.get());
            log.info("Genre: {}", genreDto.toString());
            return genreDto;
        } else {
            log.error("Genre with name {} not found", name);
            throw new NoSuchElementException("No value present");
        }
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

    private GenreDto convertEntityToDto(Genre genre){
        List<BookDto> bookDtoList = null;
        if(genre.getBooks() != null){

            bookDtoList = genre.getBooks()
                    .stream()
                    .map(book -> BookDto.builder()
                            .name(book.getName())
                            .id(book.getId())
                            .build())
                    .toList();
        }
        GenreDto genreDto = GenreDto.builder()
                .id(genre.getId())
                .genre(genre.getName())
                .build();
        return genreDto;
    }
}
