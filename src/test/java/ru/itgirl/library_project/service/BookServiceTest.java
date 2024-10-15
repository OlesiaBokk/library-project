package ru.itgirl.library_project.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.itgirl.library_project.dto.BookCreateDto;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.dto.BookUpdateDto;
import ru.itgirl.library_project.model.entity.Author;
import ru.itgirl.library_project.model.entity.Book;
import ru.itgirl.library_project.model.entity.Genre;
import ru.itgirl.library_project.repository.BookRepository;
import ru.itgirl.library_project.repository.GenreRepository;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private GenreRepository genreRepository;
    @InjectMocks
    private BookServiceImpl bookService;

    private Book setBookFields() {
        Long id = 1L;
        String name = "SimpleBook";
        Genre genre = new Genre(1L, "GenreName", Collections.emptyList(), Collections.emptyList());
        Set<Author> authors = new HashSet<>();
        return new Book(id, name, genre, authors);
    }

    @Test
    public void testGetBookById() {
        Book book = setBookFields();
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        BookDto bookDto = bookService.getBookById(book.getId());
        verify(bookRepository).findById(book.getId());
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
        Assertions.assertEquals(bookDto.getGenre(), book.getGenre().getName());
    }

    @Test
    public void testGetBookByIdNotFound() {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.getBookById(id));
        verify(bookRepository).findById(id);
    }

    @Test
    public void testGetByNameV1() {
        Book book = setBookFields();
        when(bookRepository.findBookByName(book.getName())).thenReturn(Optional.of(book));
        BookDto bookDto = bookService.getByNameV1(book.getName());
        verify(bookRepository).findBookByName(book.getName());
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
        Assertions.assertEquals(bookDto.getGenre(), book.getGenre().getName());
    }

    @Test
    public void testGetBookByNameV1NotFound() {
        String name = "SimpleBook";
        when(bookRepository.findBookByName(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.getByNameV1(name));
        verify(bookRepository).findBookByName(name);
    }

    @Test
    public void testGetByNameV2() {
        Book book = setBookFields();
        when(bookRepository.findBookByNameBySql(book.getName())).thenReturn(Optional.of(book));
        BookDto bookDto = bookService.getByNameV2(book.getName());
        verify(bookRepository).findBookByNameBySql(book.getName());
        Assertions.assertEquals(bookDto.getId(), book.getId());
        Assertions.assertEquals(bookDto.getName(), book.getName());
        Assertions.assertEquals(bookDto.getGenre(), book.getGenre().getName());
    }

    @Test
    public void testGetBookByNameV2NotFound() {
        String name = "SimpleBook";
        when(bookRepository.findBookByNameBySql(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.getByNameV2(name));
        verify(bookRepository).findBookByNameBySql(name);
    }

    @Test
    public void testCreateBook() {
        Book book = setBookFields();
        BookCreateDto bookCreateDto = new BookCreateDto(book.getName(), book.getGenre().getName());
        when(genreRepository.findByName("GenreName")).thenReturn(Optional.of(book.getGenre()));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        BookDto result = bookService.createBook(bookCreateDto);
        Assertions.assertEquals(result.getId(), book.getId());
        Assertions.assertEquals(result.getName(), book.getName());
        Assertions.assertEquals(result.getGenre(), book.getGenre().getName());
    }

    @Test
    public void testUpdateBook() {
        Book book = setBookFields();
        when(genreRepository.findByName("GenreName")).thenReturn(Optional.of(book.getGenre()));
        when(genreRepository.findByName("NewGenre")).thenReturn(Optional.of(
                new Genre(2L, "NewGenre", Collections.emptyList(), Collections.emptyList())));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        BookUpdateDto bookUpdateDto = new BookUpdateDto(book.getId(), "NewName", "NewGenre");
        book.setName(bookUpdateDto.getName());
        book.setGenre(genreRepository.findByName(bookUpdateDto.getGenre()).orElseThrow());
        BookDto result = bookService.updateBook(bookUpdateDto);
        Assertions.assertEquals(result.getId(), book.getId());
        Assertions.assertEquals(result.getName(), book.getName());
        Assertions.assertEquals(result.getGenre(), book.getGenre().getName());
    }

    @Test
    public void testUpdateBookNotFound() {
        Book book = new Book();
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        BookUpdateDto bookUpdateDto = new BookUpdateDto(book.getId(), "NewName", "NewGenre");
        Assertions.assertThrows(RuntimeException.class, () -> bookService.updateBook(bookUpdateDto));
    }

    @Test
    public void testDeleteBook() {
        Book book = setBookFields();
        Long id = book.getId();
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        bookRepository.deleteById(id);
        verify(bookRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteBookNotFound() {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> bookService.deleteBook(id));
        verify(bookRepository, times(0)).deleteById(anyLong());
    }

}
