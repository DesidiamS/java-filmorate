package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private Long id;
    @Email
    private String email;
    @Pattern(regexp = ".*\\S+.*")
    @NotNull
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
