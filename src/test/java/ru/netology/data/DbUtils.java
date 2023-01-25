package ru.netology.data;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import ru.netology.models.PaymentRequest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtils {
    private static final String URL = System.getProperty("db.url");
    private static final String USERNAME = System.getProperty("db.username");
    private static final String PASSWORD = System.getProperty("db.password");
    private static Connection connect;

    private static Connection getConnection() {
        try {
            connect = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return connect;
    }

    public static String getPaymentStatus() {
        var runner = new QueryRunner();
        var payStatus = "SELECT status FROM payment_entity";

        try (var connect = getConnection()) {
            var paymentStatus = runner.query(connect, payStatus, new BeanHandler<>(PaymentRequest.class));
            return paymentStatus.getStatus();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }
}