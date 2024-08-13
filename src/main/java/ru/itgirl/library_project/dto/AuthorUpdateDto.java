package ru.itgirl.library_project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthorUpdateDto {
    private Long id;
    @Size(min = 3, max = 10)
    @NotBlank(message = "Необходимо указать имя")
    private String name;
    @NotBlank(message = "Необходимо указать имя")
    private String surname;
}