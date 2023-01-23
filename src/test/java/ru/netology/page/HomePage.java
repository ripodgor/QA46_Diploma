package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class HomePage {
    private SelenideElement buyButton = $(withText("Купить"));
    private SelenideElement buyOnCreditButton = $(withText("Купить в кредит"));

    public BuyPage buy() {
        buyButton.click();
        return new BuyPage();
    }

    public BuyOnCreditPage buyOnCredit() {
        buyOnCreditButton.click();
        return new BuyOnCreditPage();
    }
}