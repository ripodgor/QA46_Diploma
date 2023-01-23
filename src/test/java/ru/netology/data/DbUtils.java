package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;

public class DbUtils {
    public DbUtils() {
    }

    @SneakyThrows
    public static void clearDb() {
        var paymentSQL = "DELETE FROM payment_entity";
        var creditSQL = "DELETE FROM credit_request_entity";
        var orderSQL = "DELETE FROM order_entity";
        var runner = new QueryRunner();

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            runner.execute(conn, paymentSQL, new ScalarHandler<>());
            runner.execute(conn, creditSQL, new ScalarHandler<>());
            runner.execute(conn, orderSQL, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        var statusSQL = "SELECT status FROM payment_entity";
        var runner = new QueryRunner();
        String paymentStatus;

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
        ) {
            var status = runner.query(conn, statusSQL, new ScalarHandler<>());
            paymentStatus = (String) status;
        }
        return paymentStatus;
    }
}