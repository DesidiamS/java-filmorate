package ru.yandex.practicum.filmorate.exceptions;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {
    String errorMsg;

    public ErrorResponse(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
