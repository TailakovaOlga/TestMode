package ru.netology.testmode;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.DataGenerator.Registration.getUser;

public class TestMode {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Successful authorization of the registered user")
    void shouldSuccessfulLoginIfRegisteredUser() {
        var registeredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $(".button").click();
        $("h2").shouldHave(Condition.exactText("Личный кабинет")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIBlockedUser() {
        var blockedUser = getUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $(".button").click();
        $(".notification_status_error .notification__content")
                .shouldHave(text("Ошибка! Пользователь заблокирован!"))
                .shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegistredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $(".button").click();
        $("[data-test-id='error-notification'] .notification__content")
                .shouldHave(text("Ошибка! " + "Неверно указан логин или пароль"))
                .shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with wrong  password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(wrongPassword());
        $(".button").click();
        $(".notification_status_error .notification__content")
                .shouldHave(text("Ошибка! " + "Неверно указан логин или пароль"))
                .shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(DataGenerator.generateLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $(".button").click();
        $(".notification_status_error .notification__content")
                .shouldHave(text("Ошибка! " + "Неверно указан логин или пароль"))
                .shouldBe(visible, Duration.ofSeconds(15));
    }
}
