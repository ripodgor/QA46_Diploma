package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataGenerator.Info;

import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class BuyOnCreditPage {
    private SelenideElement buyHeader = $(byText("Кредит по данным карты"));
    private SelenideElement cardNumberField = $(byText("Номер карты")).parent().$(".input__control");
    private SelenideElement monthField = $(byText("Месяц")).parent().$(".input__control");
    private SelenideElement yearField = $(byText("Год")).parent().$(".input__control");
    private SelenideElement ownerField = $(byText("Владелец")).parent().$(".input__control");
    private SelenideElement cvcField = $(byText("CVC/CVV")).parent().$(".input__control");
    private SelenideElement continueButton = $(byText("Продолжить"));
    private SelenideElement notificationSuccess = $(".notification_status_ok");
    private SelenideElement notificationError = $(".notification_status_error");
    private SelenideElement cardNumberSub = $(byText("Номер карты")).parent().$(".input__sub");
    private SelenideElement monthSub = $(byText("Месяц")).parent().$(".input__sub");
    private SelenideElement yearSub = $(byText("Год")).parent().$(".input__sub");
    private SelenideElement expiredCardSub = $(byText("Истек срок действия карты")).parent().$(".input__sub");
    private SelenideElement ownerSub = $(byText("Владелец")).parent().$(".input__sub");
    private SelenideElement cvcSub = $(byText("CVC/CVV")).parent().$(".input__sub");

    public BuyOnCreditPage() {
        buyHeader.shouldBe(visible);
    }

    public void sendFilledForm(Info info) {
        cardNumberField.setValue(info.getNumber());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getOwner());
        cvcField.setValue(info.getCvc());
        continueButton.click();
    }

    public void successfulPayment() {
        notificationSuccess.shouldHave(text("Операция одобрена Банком."), Duration.ofSeconds(12));
    }

    public void declinedPayment() {
        notificationError.shouldHave(text("Ошибка! Банк отказал в проведении операции."), Duration.ofSeconds(12));
    }

    public void emptyForm() {
        continueButton.click();
        cardNumberSub.shouldHave(text("Поле обязательно для заполнения"));
        monthSub.shouldHave(text("Поле обязательно для заполнения"));
        yearSub.shouldHave(text("Поле обязательно для заполнения"));
        ownerSub.shouldHave(text("Поле обязательно для заполнения"));
        cvcSub.shouldHave(text("Поле обязательно для заполнения"));
    }

    public void emptyCardNumberError() {
        cardNumberSub.shouldHave(text("Поле обязательно для заполнения"));
    }

    public void wrongCardNumberFormat() {
        cardNumberSub.shouldHave(text("Неверный формат"));
    }

    public int getCardNumberDigitsCount() {
        return Objects.requireNonNull(cardNumberField.getValue()).length();
    }

    public void emptyMonthError() {
        monthSub.shouldHave(text("Поле обязательно для заполнения"));
    }

    public void outOfBoundsMonthError() {
        monthSub.shouldHave(text("Неверно указан срок действия карты"));
    }

    public void wrongMonthFormat() {
        monthSub.shouldHave(text("Неверный формат"));
    }

    public int getMonthDigitsCount() {
        return Objects.requireNonNull(monthField.getValue()).length();
    }

    public void emptyYearError() {
        yearSub.shouldHave(text("Поле обязательно для заполнения"));
    }

    public void wrongYearFormat() {
        yearSub.shouldHave(text("Неверный формат"));
    }

    public void pastYearError() {
        yearSub.shouldHave(text("Истёк срок действия карты"));
    }

    public void wrongYearError() {
        yearSub.shouldHave(text("Неверно указан срок действия карты"));
    }

    public int getYearDigitsCount() {
        return Objects.requireNonNull(yearField.getValue()).length();
    }

    public void emptyOwnerError() {
        ownerSub.shouldHave(text("Поле обязательно для заполнения"));
    }

    public void wrongOwnerFormat() {
        ownerSub.shouldHave(text("Неверный формат"));
    }

    public int getOwnerLettersCount() {
        return Objects.requireNonNull(ownerField.getValue()).length();
    }

    public String getOwnerField() {
        return ownerField.getValue();
    }

    public void emptyCvcError() {
        cvcSub.shouldHave(text("Поле обязательно для заполнения"));
    }

    public int getCvcDigitsCount() {
        return Objects.requireNonNull(cvcField.getValue()).length();
    }

    public void wrongCvcFormat() {
        cvcSub.shouldHave(text("Неверный формат"));
    }
}