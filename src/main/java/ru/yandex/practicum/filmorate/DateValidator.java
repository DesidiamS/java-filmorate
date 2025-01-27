package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<IsAfter, LocalDate> {

    String validDate;

    @Override
    public void initialize(IsAfter constraintAnnotation) {
        validDate = constraintAnnotation.current();
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        String[] splitDate = validDate.split("-");
        //String pattern = "yyyy-MM-dd";
        //DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        //LocalDate localDateTime = LocalDate.parse(validDate, dateTimeFormatter);
        //Instant instant =localDateTime.atStartOfDay(ZoneId.systemDefault()).toInstant();
        //return date.isAfter(instant);
        return date.isAfter(LocalDate.of(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2])));
    }
}
