package ru.itgirl.library_project.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.itgirl.library_project.dto.BookCreateDto;
import ru.itgirl.library_project.dto.BookDto;
import ru.itgirl.library_project.dto.BookUpdateDto;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BookRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private BookDto setBookDtoFields() {
        return new BookDto(1L, "Война и мир", "Роман");
    }

    @Test
    public void testGetBookById() throws Exception {
        BookDto bookDto = setBookDtoFields();
        mockMvc.perform(MockMvcRequestBuilders.get("/book/{id}", bookDto.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookDto.getGenre()));
    }

    @Test
    public void testGetBookByName() throws Exception {
        BookDto bookDto = setBookDtoFields();
        String name = "Война и мир";
        mockMvc.perform(MockMvcRequestBuilders.get("/book?name={name}", name))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookDto.getGenre()));
    }

    @Test
    public void testGetBookByNameV2() throws Exception {
        BookDto bookDto = setBookDtoFields();
        String name = "Война и мир";
        mockMvc.perform(MockMvcRequestBuilders.get("/book/v2?name={name}", name))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookDto.getGenre()));
    }

    @Test
    public void testGetBookByNameV3() throws Exception {
        BookDto bookDto = setBookDtoFields();
        String name = "Война и мир";
        mockMvc.perform(MockMvcRequestBuilders.get("/book/v3?name={name}", name))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookDto.getGenre()));
    }

    @Test
    public void testCreateBook() throws Exception {
        BookCreateDto bookCreateDto = new BookCreateDto();
        bookCreateDto.setName("BookName");
        bookCreateDto.setGenre("GenreName");
        mockMvc.perform(MockMvcRequestBuilders.post("/book/create", bookCreateDto))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookCreateDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookCreateDto.getGenre()));
    }

    @Test
    public void testUpdateBook() throws Exception {
        BookUpdateDto bookUpdateDto = new BookUpdateDto();
        bookUpdateDto.setName("UpdatedName");
        bookUpdateDto.setGenre("UpdatedGenre");
        mockMvc.perform(MockMvcRequestBuilders.put("/book/update", bookUpdateDto))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(bookUpdateDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(bookUpdateDto.getGenre()));
    }

    @Test
    public void testDeleteBook() throws Exception {
        Long id = 1L;
        ResultActions resp = mockMvc.perform(MockMvcRequestBuilders.delete("/book/delete/{id}", id));
        resp.andExpect(status().isOk());
    }
}
