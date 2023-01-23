package ru.netology.data;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;

import static io.restassured.RestAssured.given;

public class ApiUtils {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(8080)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public ApiUtils() {
    }

    @SneakyThrows
    public static String getStatusOfGivenCards(DataGenerator.Info info) {
        RequestSpecification request = given();
        request.spec(requestSpec)// указываем, какую спецификацию используем
                .body(info)  // передаём в теле объект, который будет преобразован в JSON
                .then()
                .statusCode(200); // промежуточная проверка
        var response = request.post("/api/v1/pay");
        return response.path("status");
    }

    @SneakyThrows
    public static void getStatusCodeOfUnknownCards(DataGenerator.Info info) {
        given()
                .spec(requestSpec)// указываем, какую спецификацию используем
                .body(info)  // передаём в теле объект, который будет преобразован в JSON
                .when()
                .post("/api/v1/pay")
                .then()
                .statusCode(400); // промежуточная проверка
    }
}