package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.IsAfter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static ru.yandex.practicum.filmorate.Constants.MAX_SIZE;
import static ru.yandex.practicum.filmorate.Constants.MIN_DATE;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    Long id;
    @NotBlank
    String name;
    @Size(max = MAX_SIZE)
    String description;
    @IsAfter(current = MIN_DATE)
    LocalDate releaseDate;
    @Min(1)
    Long duration;
    Set<Long> usersLikes = new HashSet<>();
}
