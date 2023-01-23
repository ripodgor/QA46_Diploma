package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator.Info;
import ru.netology.data.DbUtils;
import ru.netology.page.HomePage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.Info.*;

public class UiTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:8080");
        DbUtils.clearDb();
    }

    // Позитивные сценарии

    @Test
    void shouldPayWithApprovedSuccessfully() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.successfulPayment();
        assertEquals("APPROVED", DbUtils.getPaymentStatus());
    }

    @Test
    void shouldNotPayWithDeclined() {
        var info = new Info(getDeclinedCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.declinedPayment();
        assertEquals("DECLINED", DbUtils.getPaymentStatus());
    }

    // Негативные сценарии
    // Поле "Номер карты"
    @Test
    void shouldNotSendEmptyForm() {
        var buyPage = new HomePage().buy();
        buyPage.emptyForm();
    }

    @Test
    void shouldNotSendWithEmptyNumber() {
        var info = new Info(null, getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.emptyCardNumberError();
    }

    @Test
    void shouldNotPayWithUnknownCardNumber() {
        var info = new Info(getUnknownCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.declinedPayment();
    }

    @Test
    void shouldNotSendShortCardNumber() {
        var info = new Info(getShortCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.wrongCardNumberFormat();
    }

    @Test
    void shouldLimitCardNumber() {
        var info = new Info(getLongCardNumber(), null, null, null, null);
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        assertEquals(19, buyPage.getCardNumberDigitsCount());
    }

    @Test
    void shouldNotPayWithCardNumberFromZeroes() {
        var info = new Info(getCardNumberFromZeroes(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.declinedPayment();
    }

    @Test
    void shouldNotAcceptSymbolsInCardNumber() {
        var info = new Info(getCardNumberFromSymbols(), null, null, null, null);
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        assertEquals(0, buyPage.getCardNumberDigitsCount());
    }

    @Test
    void shouldNotAcceptLettersInCardNumber() {
        var info = new Info(getCardNumberFromLetters(), null, null, null, null);
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        assertEquals(0, buyPage.getCardNumberDigitsCount());
    }

    // Поле "Месяц"
    @Test
    void shouldNotSendWithEmptyMonth() {
        var info = new Info(getApprovedCardNumber(), null, getValidYear(), getValidOwner(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.emptyMonthError();
    }

    @Test
    void shouldNotSendWithMonthOfZeroes() {
        var info = new Info(getApprovedCardNumber(), getMonthWithZeroes(), getValidYear(), getValidOwner(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.outOfBoundsMonthError();
    }

    @Test
    void shouldNotSendWithMonthOverTwelve() {
        var info = new Info(getApprovedCardNumber(), getMonthOverTwelve(), getValidYear(), getValidOwner(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.outOfBoundsMonthError();
    }

    @Test
    void shouldNotSendWithOneDigitMonth() {
        var info = new Info(getApprovedCardNumber(), getOneDigitMonth(), getValidYear(), getValidOwner(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.wrongMonthFormat();
    }

    @Test
    void shouldNotAcceptLettersInMonth() {
        var info = new Info(null, getMonthFromLetters(), null, null, null);
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        assertEquals(0, buyPage.getMonthDigitsCount());
    }

    @Test
    void shouldNotAcceptSymbolsInMonth() {
        var info = new Info(null, getMonthFromSymbols(), null, null, null);
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        assertEquals(0, buyPage.getCardNumberDigitsCount());
    }

    // Поле "Год"
    @Test
    void shouldNotSendWithEmptyYear() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), null, getValidOwner(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.emptyYearError();
    }

    @Test
    void shouldNotSendWithOneDigitYear() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getOneDigitYear(), getValidOwner(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.wrongYearFormat();
    }

    @Test
    void shouldNotSendWithPastYear() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getPastYear(), getValidOwner(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.pastYearError();
    }

    @Test
    void shouldNotSendWithDistantFutureYear() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getDistantFutureYear(), getValidOwner(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.wrongYearError();
    }

    @Test
    void shouldNotSendWithYearOfZeroes() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getYearWithZeroes(), getValidOwner(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.pastYearError();
    }

    @Test
    void shouldNotAcceptLettersInYear() {
        var info = new Info(null, null, getYearFromLetters(), null, null);
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        assertEquals(0, buyPage.getYearDigitsCount());
    }

    @Test
    void shouldNotAcceptSymbolsInYear() {
        var info = new Info(null, null, getYearFromSymbols(), null, null);
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        assertEquals(0, buyPage.getYearDigitsCount());
    }

    // Поле "Владелец"
    @Test
    void shouldNotSendWithEmptyOwner() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), null, getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.emptyOwnerError();
    }

    @Test
    void shouldNotSendWithShortOwnerName() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getShortOwnerName(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.wrongOwnerFormat();
    }

    @Test
    void shouldNotSendWithLongOwnerName() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getLongOwnerName(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        assertEquals(29, buyPage.getOwnerLettersCount());
    }

    @Test
    void shouldNotSendWithCyrillicOwnerName() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getCyrillicName(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.wrongOwnerFormat();
    }

    @Test
    void shouldNotSendWithFirstNameOnly() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getFirstNameOnly(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.wrongOwnerFormat();
    }

    @Test
    void shouldNotSendWithDigitsForName() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getDigitsForName(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        assertEquals(0, buyPage.getOwnerLettersCount());
    }

    @Test
    void shouldNotSendWithSymbolsForName() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getNameFromSymbols(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        assertEquals(0, buyPage.getOwnerLettersCount());
    }

    @Test
    void shouldNotSendWithDashedName() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getDashedName(), getValidCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        assertEquals("Kuzma Petrov-Vodkin", buyPage.getOwnerField());
    }

    // Поле "CVC/CVV"
    @Test
    void shouldNotSendWithEmptyCvc() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), null);
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.emptyCvcError();
    }

    @Test
    void shouldNotAcceptLettersInCvc() {
        var info = new Info(null, null, null, null, getLettersForCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        assertEquals(0, buyPage.getCvcDigitsCount());
    }

    @Test
    void shouldNotAcceptSymbolsInCvc() {
        var info = new Info(null, null, null, null, getSymbolsForCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        assertEquals(0, buyPage.getCvcDigitsCount());
    }

    @Test
    void shouldNotSendWithOneDigitCvc() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getOneDigitCvc());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        buyPage.wrongCvcFormat();
    }

    @Test
    void shouldNotAcceptOverThreeDigitsInCvc() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getCvcOverThreeDigits());
        var buyPage = new HomePage().buy();
        buyPage.sendFilledForm(info);
        assertEquals(3, buyPage.getCvcDigitsCount());
    }
}