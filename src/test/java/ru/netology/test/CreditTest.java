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

public class CreditTest {

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
    }

    // Позитивные сценарии

    @Test
    void shouldPayWithApprovedSuccessfully() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.successfulPayment();
        assertEquals("APPROVED", DbUtils.getPaymentStatus());
    }

    @Test
    void shouldNotPayWithDeclined() {
        var info = new Info(getDeclinedCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.declinedPayment();
        assertEquals("DECLINED", DbUtils.getPaymentStatus());
    }

    // Негативные сценарии
    // Поле "Номер карты"
    @Test
    void shouldNotSendEmptyForm() {
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.emptyForm();
    }

    @Test
    void shouldNotSendWithEmptyNumber() {
        var info = new Info(null, getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.emptyCardNumberError();
    }

    @Test
    void shouldNotPayWithUnknownCardNumber() {
        var info = new Info(getUnknownCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.declinedPayment();
    }

    @Test
    void shouldNotSendShortCardNumber() {
        var info = new Info(getShortCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.wrongCardNumberFormat();
    }

    @Test
    void shouldLimitCardNumber() {
        var info = new Info(getLongCardNumber(), null, null, null, null);
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        assertEquals(19, buyOnCreditPage.getCardNumberDigitsCount());
    }

    @Test
    void shouldNotPayWithCardNumberFromZeroes() {
        var info = new Info(getCardNumberFromZeroes(), getValidMonth(), getValidYear(), getValidOwner(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.declinedPayment();
    }

    @Test
    void shouldNotAcceptSymbolsInCardNumber() {
        var info = new Info(getCardNumberFromSymbols(), null, null, null, null);
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        assertEquals(0, buyOnCreditPage.getCardNumberDigitsCount());
    }

    @Test
    void shouldNotAcceptLettersInCardNumber() {
        var info = new Info(getCardNumberFromLetters(), null, null, null, null);
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        assertEquals(0, buyOnCreditPage.getCardNumberDigitsCount());
    }

    // Поле "Месяц"
    @Test
    void shouldNotSendWithEmptyMonth() {
        var info = new Info(getApprovedCardNumber(), null, getValidYear(), getValidOwner(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.emptyMonthError();
    }

    @Test
    void shouldNotSendWithMonthOfZeroes() {
        var info = new Info(getApprovedCardNumber(), getMonthWithZeroes(), getValidYear(), getValidOwner(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.outOfBoundsMonthError();
    }

    @Test
    void shouldNotSendWithMonthOverTwelve() {
        var info = new Info(getApprovedCardNumber(), getMonthOverTwelve(), getValidYear(), getValidOwner(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.outOfBoundsMonthError();
    }

    @Test
    void shouldNotSendWithOneDigitMonth() {
        var info = new Info(getApprovedCardNumber(), getOneDigitMonth(), getValidYear(), getValidOwner(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.wrongMonthFormat();
    }

    @Test
    void shouldNotAcceptLettersInMonth() {
        var info = new Info(null, getMonthFromLetters(), null, null, null);
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        assertEquals(0, buyOnCreditPage.getMonthDigitsCount());
    }

    @Test
    void shouldNotAcceptSymbolsInMonth() {
        var info = new Info(null, getMonthFromSymbols(), null, null, null);
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        assertEquals(0, buyOnCreditPage.getCardNumberDigitsCount());
    }

    // Поле "Год"
    @Test
    void shouldNotSendWithEmptyYear() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), null, getValidOwner(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.emptyYearError();
    }

    @Test
    void shouldNotSendWithOneDigitYear() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getOneDigitYear(), getValidOwner(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.wrongYearFormat();
    }

    @Test
    void shouldNotSendWithPastYear() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getPastYear(), getValidOwner(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.pastYearError();
    }

    @Test
    void shouldNotSendWithDistantFutureYear() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getDistantFutureYear(), getValidOwner(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.wrongYearError();
    }

    @Test
    void shouldNotSendWithYearOfZeroes() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getYearWithZeroes(), getValidOwner(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.pastYearError();
    }

    @Test
    void shouldNotAcceptLettersInYear() {
        var info = new Info(null, null, getYearFromLetters(), null, null);
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        assertEquals(0, buyOnCreditPage.getYearDigitsCount());
    }

    @Test
    void shouldNotAcceptSymbolsInYear() {
        var info = new Info(null, null, getYearFromSymbols(), null, null);
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        assertEquals(0, buyOnCreditPage.getYearDigitsCount());
    }

    // Поле "Владелец"
    @Test
    void shouldNotSendWithEmptyOwner() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), null, getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.emptyOwnerError();
    }

    @Test
    void shouldNotSendWithShortOwnerName() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getShortOwnerName(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.wrongOwnerFormat();
    }

    @Test
    void shouldNotSendWithLongOwnerName() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getLongOwnerName(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        assertEquals(29, buyOnCreditPage.getOwnerLettersCount());
    }

    @Test
    void shouldNotSendWithCyrillicOwnerName() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getCyrillicName(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.wrongOwnerFormat();
    }

    @Test
    void shouldNotSendWithFirstNameOnly() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getFirstNameOnly(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.wrongOwnerFormat();
    }

    @Test
    void shouldNotSendWithDigitsForName() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getDigitsForName(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        assertEquals(0, buyOnCreditPage.getOwnerLettersCount());
    }

    @Test
    void shouldNotSendWithSymbolsForName() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getNameFromSymbols(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        assertEquals(0, buyOnCreditPage.getOwnerLettersCount());
    }

    @Test
    void shouldNotSendWithDashedName() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getDashedName(), getValidCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        assertEquals("Kuzma Petrov-Vodkin", buyOnCreditPage.getOwnerField());
    }

    // Поле "CVC/CVV"
    @Test
    void shouldNotSendWithEmptyCvc() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), null);
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.emptyCvcError();
    }

    @Test
    void shouldNotAcceptLettersInCvc() {
        var info = new Info(null, null, null, null, getLettersForCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        assertEquals(0, buyOnCreditPage.getCvcDigitsCount());
    }

    @Test
    void shouldNotAcceptSymbolsInCvc() {
        var info = new Info(null, null, null, null, getSymbolsForCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        assertEquals(0, buyOnCreditPage.getCvcDigitsCount());
    }

    @Test
    void shouldNotSendWithOneDigitCvc() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getOneDigitCvc());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        buyOnCreditPage.wrongCvcFormat();
    }

    @Test
    void shouldNotAcceptOverThreeDigitsInCvc() {
        var info = new Info(getApprovedCardNumber(), getValidMonth(), getValidYear(), getValidOwner(), getCvcOverThreeDigits());
        var buyOnCreditPage = new HomePage().buyOnCredit();
        buyOnCreditPage.sendFilledForm(info);
        assertEquals(3, buyOnCreditPage.getCvcDigitsCount());
    }
}