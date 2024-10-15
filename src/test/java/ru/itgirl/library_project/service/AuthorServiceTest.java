package ru.itgirl.library_project.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.itgirl.library_project.dto.AuthorCreateDto;
import ru.itgirl.library_project.dto.AuthorDto;
import ru.itgirl.library_project.dto.AuthorUpdateDto;
import ru.itgirl.library_project.model.entity.Author;
import ru.itgirl.library_project.model.entity.Book;
import ru.itgirl.library_project.repository.AuthorRepository;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author setAuthorFields() {
        Long id = 1L;
        String name = "Harry";
        String surname = "Potter";
        Set<Book> books = new HashSet<>();
        return new Author(id, name, surname, books);
    }

    @Test
    public void testGetAuthorById() {
        Author author = setAuthorFields();
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        AuthorDto authorDto = authorService.getAuthorById(author.getId());
        verify(authorRepository).findById(author.getId());
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetAuthorByIdNotFound() {
        Long id = 1L;
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorById(id));
        verify(authorRepository).findById(id);
    }

    @Test
    public void testGetAuthorByName() {
        Author author = setAuthorFields();
        when(authorRepository.findAuthorByName(author.getName())).thenReturn(Optional.of(author));
        AuthorDto authorDto = authorService.getAuthorByName(author.getName());
        verify(authorRepository).findAuthorByName(author.getName());
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetAuthorByNameNotFound() {
        String name = "Harry";
        when(authorRepository.findAuthorByName(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorByName(name));
        verify(authorRepository).findAuthorByName(name);
    }

    @Test
    public void testGetAuthorByNameV2() {
        Author author = setAuthorFields();
        when(authorRepository.findAuthorByNameBySql(author.getName())).thenReturn(Optional.of(author));
        AuthorDto authorDto = authorService.getAuthorByNameV2(author.getName());
        verify(authorRepository).findAuthorByNameBySql(author.getName());
        Assertions.assertEquals(authorDto.getId(), author.getId());
        Assertions.assertEquals(authorDto.getName(), author.getName());
        Assertions.assertEquals(authorDto.getSurname(), author.getSurname());
    }

    @Test
    public void testGetAuthorByNameV2NotFound() {
        String name = "Harry";
        when(authorRepository.findAuthorByNameBySql(name)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.getAuthorByNameV2(name));
        verify(authorRepository).findAuthorByNameBySql(name);
    }

    @Test
    public void testCreateAuthor() {
        Author author = setAuthorFields();
        AuthorCreateDto authorCreateDto = new AuthorCreateDto(author.getName(), author.getSurname());
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        AuthorDto result = authorService.createAuthor(authorCreateDto);
        Assertions.assertEquals(result.getId(), author.getId());
        Assertions.assertEquals(result.getName(), author.getName());
        Assertions.assertEquals(result.getSurname(), author.getSurname());
    }

    @Test
    public void testUpdateAuthor() {
        Author author = setAuthorFields();
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto(author.getId(), "NewName", "NewSurname");
        author.setName(authorUpdateDto.getName());
        author.setSurname(authorUpdateDto.getSurname());
        AuthorDto result = authorService.updateAuthor(authorUpdateDto);
        Assertions.assertEquals(result.getId(), author.getId());
        Assertions.assertEquals(result.getName(), author.getName());
        Assertions.assertEquals(result.getSurname(), author.getSurname());
    }

    @Test
    public void testUpdateAuthorNotFound() {
        Author author = new Author();
        Long id = 1L;
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto(author.getId(), "NewName", "NewSurname");
        Assertions.assertThrows(RuntimeException.class, () -> authorService.updateAuthor(authorUpdateDto));
    }

    @Test
    public void testDeleteAuthor() {
        Author author = setAuthorFields();
        Long id = author.getId();
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        authorRepository.deleteById(id);
        verify(authorRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteAuthorNotFound() {
        Long id = 1L;
        when(authorRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> authorService.deleteAuthor(id));
        verify(authorRepository, times(0)).deleteById(anyLong());
    }
}
