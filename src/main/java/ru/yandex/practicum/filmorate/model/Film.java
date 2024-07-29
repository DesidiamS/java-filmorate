package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.IsAfter;

import java.time.Duration;
import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.Constants.MAX_SIZE;
import static ru.yandex.practicum.filmorate.Constants.MIN_DATE;


@Data
public class Film {

    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @Size(max = MAX_SIZE)
    private String description;
    @IsAfter(current = MIN_DATE)
    private LocalDate releaseDate;
    @DurationMin
    private Duration duration;
}
