package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.ApiUtils;
import ru.netology.data.DataGenerator;
import ru.netology.data.DbUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.Info.*;

public class ApiTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldPayWithApprovedSuccessfully() {
        var info = new DataGenerator.Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        assertEquals("APPROVED", ApiUtils.getStatusOfGivenCards(info));
        assertEquals("APPROVED", DbUtils.getPaymentStatus());
    }

    @Test
    void shouldPayWithApprovedAndCvvZeroesSuccessfully() {
        var info = new DataGenerator.Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getCvcFromZeroes());
        assertEquals("APPROVED", ApiUtils.getStatusOfGivenCards(info));
        assertEquals("APPROVED", DbUtils.getPaymentStatus());
    }

    @Test
    void shouldNotPayWithDeclined() {
        var info = new DataGenerator.Info(getDeclinedCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        assertEquals("DECLINED", ApiUtils.getStatusOfGivenCards(info));
        assertEquals("DECLINED", DbUtils.getPaymentStatus());
    }

    @Test
    void shouldNotPayWithUnknownCardNumber() {
        var info = new DataGenerator.Info(getUnknownCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        ApiUtils.getStatusCodeOfUnknownCards(info);
    }

    @Test
    void shouldNotPayWithCardNumberFromZeroes() {
        var info = new DataGenerator.Info(getCardNumberFromZeroes(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        ApiUtils.getStatusCodeOfUnknownCards(info);
    }
}