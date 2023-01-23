package ru.netology.data;

import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.Year;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    public static Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

    @Value
    @RequiredArgsConstructor
    public static class Info {
        String number;
        String month;
        String year;
        String owner;
        String cvc;

        // Поле "Номер карты"
        public static String getApprovedCardNumber() {
            return "4444 4444 4444 4441";
        }

        public static String getDeclinedCardNumber() {
            return "4444 4444 4444 4442";
        }

        public static String getUnknownCardNumber() {
            return faker.finance().creditCard(CreditCardType.MASTERCARD);
        }

        public static String getShortCardNumber() {
            return faker.finance().creditCard(CreditCardType.AMERICAN_EXPRESS);
        }

        public static String getLongCardNumber() {
            return faker.finance().creditCard(CreditCardType.SOLO);
        }

        public static String getCardNumberFromZeroes() {
            return "0000 0000 0000 0000";
        }

        public static String getCardNumberFromSymbols() {
            String result = "";
            for (int i = 0; i < 16; i++) {
                int randomInt = new Random().nextInt(14) + 33;
                result += (char) randomInt;
            }
            return result;
        }

        public static String getCardNumberFromLetters() {
            return faker.lorem().characters(16, false, false);
        }

        // Поле "Месяц"
        public static String getValidMonth() {
            return String.format("%02d", LocalDate.now().getMonthValue());
        }

        public static String getMonthWithZeroes() {
            return "00";
        }

        public static String getMonthOverTwelve() {
            return "13";
        }

        public static String getOneDigitMonth() {
            return String.valueOf(new Random().nextInt(8) + 1);
        }

        public static String getMonthFromLetters() {
            return faker.lorem().characters(2, false, false);
        }

        public static String getMonthFromSymbols() {
            String result = "";
            for (int i = 0; i < 2; i++) {
                int randomInt = new Random().nextInt(14) + 33;
                result += (char) randomInt;
            }
            return result;
        }

        // Поле "Год"
        public static String getValidYear() {
            return String.format("%ty", Year.now());
        }

        public static String getOneDigitYear() {
            return String.valueOf(new Random().nextInt(8) + 1);
        }

        public static String getPastYear() {
            return String.format("%ty", LocalDate.now().minusYears(1));
        }

        public static String getDistantFutureYear() {
            return String.format("%ty", LocalDate.now().plusYears(11));
        }

        public static String getYearWithZeroes() {
            return "00";
        }

        public static String getYearFromLetters() {
            return faker.lorem().characters(2, false, false);
        }

        public static String getYearFromSymbols() {
            String result = "";
            for (int i = 0; i < 2; i++) {
                int randomInt = new Random().nextInt(14) + 33;
                result += (char) randomInt;
            }
            return result;
        }

        // Поле "Владелец"
        public static String getValidOwner() {
            return faker.name().fullName().toUpperCase();
        }

        public static String getShortOwnerName() {
            return faker.lorem().characters(1, false, false).toUpperCase();
        }

        public static String getLongOwnerName() {
            return "Pablo Diego Jose Francisco de Paula Juan Nepomuceno Maria de los Remedios Cipriano de la Santisima Trinidad Martir Patricio Ruiz y Picasso";
        }

        public static String getCyrillicName() {
            return new Faker(new Locale("ru")).name().fullName();
        }

        public static String getFirstNameOnly() {
            return faker.name().firstName();
        }

        public static String getDigitsForName() {
            return (new Random().nextInt(10000) + 5000) + " " + (new Random().nextInt(10000) + 5000);
        }

        public static String getNameFromSymbols() {
            String result = "";
            for (int i = 0; i < 10; i++) {
                int randomInt = new Random().nextInt(14) + 33;
                result += (char) randomInt;
            }
            return result;
        }

        public static String getDashedName() {
            return "Kuzma Petrov-Vodkin";
        }

        // Поле "CVC/CVV"
        public static String getValidCvc() {
            return (String.valueOf(new Random().nextInt(899) + 100));
        }

        public static String getCvcFromZeroes() {
            return "000";
        }

        public static String getLettersForCvc() {
            return faker.lorem().characters(3, false, false);
        }

        public static String getSymbolsForCvc() {
            String result = "";
            for (int i = 0; i < 3; i++) {
                int randomInt = new Random().nextInt(14) + 33;
                result += (char) randomInt;
            }
            return result;
        }

        public static String getOneDigitCvc() {
            return String.valueOf(new Random().nextInt(8) + 1);
        }

        public static String getCvcOverThreeDigits() {
            return String.valueOf(new Random().nextInt(8999) + 1000);
        }
    }
}