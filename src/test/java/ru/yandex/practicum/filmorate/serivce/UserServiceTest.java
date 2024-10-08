package ru.yandex.practicum.filmorate.serivce;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserStorage userStorage;
    private Validator validator;

    @BeforeEach
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        userStorage = new InMemoryUserStorage();
    }

    @Test
    public void isUserCreated() {
        User user = new User();
        user.setName("Тестовый пользователь");
        user.setBirthday(LocalDate.of(2001, 11, 8));
        user.setEmail("test@yandex.ru");
        user.setLogin("SuperLogin");
        assertNotNull(userStorage.create(user));
    }

    @Test
    public void isUserUpdated() {
        User user = new User();
        user.setName("Тестовый пользователь");
        user.setBirthday(LocalDate.of(2001, 11, 8));
        user.setEmail("test@yandex.ru");
        user.setLogin("SuperLogin");
        userStorage.create(user);
        user.setLogin("SuperLogin2");
        assertEquals("SuperLogin2", userStorage.update(user).getLogin());
    }

    @Test
    public void isEmailValidated() {
        User user = new User();
        user.setName("Тестовый пользователь");
        user.setBirthday(LocalDate.of(2001, 11, 8));
        user.setEmail("test.yandex.ru");
        user.setLogin("SuperLogin");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        // Так как аннотации игнорируются внутри тестов (но работают через Postman) приходится проверять их не
        // через сервис, а вот таким образом. Если как-то можно переделать - пожалуйста, подскажите как.
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isLoginValidated() {
        User user = new User();
        user.setName("Тестовый пользователь");
        user.setBirthday(LocalDate.of(2001, 11, 8));
        user.setEmail("test.yandex.ru");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isBirthdayValidatedWithCurrentDate() {
        User user = new User();
        user.setName("Тестовый пользователь");
        user.setBirthday(LocalDate.now());
        user.setEmail("test.yandex.ru");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void isBirthdayValidatedWithFutureDate() {
        User user = new User();
        user.setName("Тестовый пользователь");
        user.setBirthday(LocalDate.of(2025, 5, 11));
        user.setEmail("test.yandex.ru");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}
