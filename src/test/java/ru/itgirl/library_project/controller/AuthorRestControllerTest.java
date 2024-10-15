package ru.itgirl.library_project.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.itgirl.library_project.dto.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthorRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private AuthorDto setAuthorDtoFields() {
        List<BookDto> books = new ArrayList<>();
        return new AuthorDto(1L, "Александр", "Пушкин", books);
    }

    @Test
    public void testGetAuthorById() throws Exception {
        AuthorDto authorDto = setAuthorDtoFields();
        mockMvc.perform(MockMvcRequestBuilders.get("/author/{id}", authorDto.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByName() throws Exception {
        AuthorDto authorDto = setAuthorDtoFields();
        String name = "Александр";
        mockMvc.perform(MockMvcRequestBuilders.get("/author?name={name}", name))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testGetAuthorByNameV2() throws Exception {
        AuthorDto authorDto = setAuthorDtoFields();
        mockMvc.perform(MockMvcRequestBuilders.get("/author/v2?name={name}", authorDto.getName()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));

    }

    @Test
    public void testGetAuthorByNameV3() throws Exception {
        AuthorDto authorDto = setAuthorDtoFields();
        mockMvc.perform(MockMvcRequestBuilders.get("/author/v3?name={name}", authorDto.getName()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(authorDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorDto.getSurname()));
    }

    @Test
    public void testCreateAuthor() throws Exception {
        AuthorCreateDto authorCreateDto = new AuthorCreateDto();
        authorCreateDto.setName("Joe");
        authorCreateDto.setSurname("JoeJoe");
        mockMvc.perform(MockMvcRequestBuilders.post("/author/create", authorCreateDto))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorCreateDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorCreateDto.getSurname()));
    }

    @Test
    public void testUpdateBook() throws Exception {
        AuthorUpdateDto authorUpdateDto = new AuthorUpdateDto();
        authorUpdateDto.setName("UpdatedName");
        authorUpdateDto.setSurname("UpdatedGenre");
        mockMvc.perform(MockMvcRequestBuilders.put("/author/update", authorUpdateDto))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(authorUpdateDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(authorUpdateDto.getSurname()));
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        Long id = 1L;
        ResultActions resp = mockMvc.perform(MockMvcRequestBuilders.delete("/author/delete/{id}", id));
        resp.andExpect(status().isOk());
    }
}