package ru.itgirl.library_project.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.itgirl.library_project.dto.AuthorDto;
import ru.itgirl.library_project.dto.BookCreateDto;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.dto.BookUpdateDto;
import ru.itgirl.library_project.model.entity.Book;
import ru.itgirl.library_project.model.entity.Genre;
import ru.itgirl.library_project.repository.BookRepository;
import ru.itgirl.library_project.repository.GenreRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public BookDto getBookById(Long id) {
        log.info("Try to find book by id {}", id);
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with id {} not found", id);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto getByNameV1(String name) {
        log.info("Try to find book by name {}", name);
        Optional<Book> book = bookRepository.findBookByName(name);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Book: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name {} not found", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto getByNameV2(String name) {
        log.info("Try to find book by nameV2 {}", name);
        Optional<Book> book = bookRepository.findBookByNameBySql(name);
        if (book.isPresent()) {
            BookDto bookDto = convertEntityToDto(book.get());
            log.info("Found book: {}", bookDto.toString());
            return bookDto;
        } else {
            log.error("Book with name {} not found", name);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public BookDto getByNameV3(String name) {
        Specification<Book> specification = Specification.where(new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("name"), name);
            }
        });

        Book book = bookRepository.findOne(specification).orElseThrow();
        return convertEntityToDto(book);
    }

    @Override
    public BookDto createBook(BookCreateDto bookCreateDto) {
        log.info("Try to create book with data {}", bookCreateDto.toString());
        try {
            Book book = bookRepository.save(convertDtoToEntity(bookCreateDto));
            BookDto bookDto = convertEntityToDto(book);
            log.info("Book with data {} successfully created", bookDto.toString());
            return bookDto;
        } catch (Exception e) {
            log.error("Failed to create book with data {}", bookCreateDto.toString());
            throw new RuntimeException("Failed to create book", e);
        }
    }

    @Override
    public BookDto updateBook(BookUpdateDto bookUpdateDto) {
        log.info("Try to update book with id {}", bookUpdateDto.getId());
        try {
            Book book = bookRepository.findById(bookUpdateDto.getId()).orElseThrow();
            book.setName(bookUpdateDto.getName());
            book.setGenre(genreRepository.findByName(bookUpdateDto.getGenre()).orElseThrow());
            Book savedBook = bookRepository.save(book);
            BookDto bookDto = convertEntityToDto(savedBook);
            log.info("Successfully updated book with data {}", savedBook);
            return bookDto;
        } catch (Exception e) {
            log.error("Failed to update book with data {}", bookUpdateDto.toString());
            throw new RuntimeException("Failed to update book", e);
        }
    }

    @Override
    public void deleteBook(Long id) {
        log.info("Try to delete book with id {}", id);
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()){
            log.info("Found book with id {}", id);
            bookRepository.deleteById(id);
            log.info("Deleted book with id {}", id);
        } else {
            log.error("Failed to find book with id {}", id);
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    private BookDto convertEntityToDto(Book book) {
        List<AuthorDto> authorDtoList = null;
        if (book.getAuthors() != null) {
            authorDtoList = book.getAuthors()
                    .stream()
                    .map(author -> AuthorDto.builder()
                            .name(author.getName())
                            .surname(author.getSurname())
                            .id(author.getId())
                            .build())
                    .toList();
        }
        BookDto bookDto = BookDto.builder()
                .id(book.getId())
                .genre(book.getGenre().getName())
                .name(book.getName())
                .build();
        return bookDto;
    }

    private Book convertDtoToEntity(BookCreateDto bookCreateDto) {
        Genre genre = genreRepository.findByName(bookCreateDto.getGenre()).orElseThrow();
        return Book.builder()
                .name(bookCreateDto.getName())
                .genre(genre)
                .build();
    }


}
