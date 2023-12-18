package ru.netology.testmode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class TestMode {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Successful authorization of the registered user")
    void shouldSuccessfulLoginIfRegisteredUser() {
        var newUser = DataGenerator.Registration.newUser("active");
        DataGenerator.patternRequest(newUser);
        $("[data-test-id='login'] input").setValue(newUser.getLogin());
        $("[data-test-id='password'] input").setValue(newUser.getPassword());
        $(".button").click();
        $("h2")
                .shouldHave(text("Личный кабинет"))
                .shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIBlockedUser() {
        var newUser = DataGenerator.Registration.newUser("blocked");
        DataGenerator.patternRequest(newUser);
        $("[data-test-id='login'] input").setValue(newUser.getLogin());
        $("[data-test-id='password'] input").setValue(newUser.getPassword());
        $(".button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! Пользователь заблокирован!"))
                .shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegistredUser() {
        var newUser = DataGenerator.Registration.newUser("active");
        $("[data-test-id='login'] input").setValue(newUser.getLogin());
        $("[data-test-id='password'] input").setValue(newUser.getPassword());
        $(".button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! " + "Неверно указан логин или пароль"))
                .shouldBe(visible, Duration.ofSeconds(15));

    }
}
