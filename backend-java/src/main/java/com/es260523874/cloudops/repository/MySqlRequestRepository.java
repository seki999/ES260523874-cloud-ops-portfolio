package com.es260523874.cloudops.repository;

import com.es260523874.cloudops.model.CloudResourceRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Docker Compose の MySQL に接続する repository です。
 * SQL は PreparedStatement に限定し、文字列連結による query 組み立てを避けます。
 */
public class MySqlRequestRepository implements CloudResourceRequestRepository {
    private final String jdbcUrl;
    private final String user;
    private final String password;

    public MySqlRequestRepository(String host, String port, String database, String user, String password) {
        this.jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        this.user = user;
        this.password = password;
        waitForDatabase();
    }

    @Override
    public List<CloudResourceRequest> findAll() {
        String sql = "SELECT request_id, project_name, resource_type, environment_name, status, requested_by, created_at FROM cloud_resource_requests ORDER BY request_id";
        List<CloudResourceRequest> requests = new ArrayList<>();
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                requests.add(mapRow(resultSet));
            }
            return requests;
        } catch (SQLException e) {
            throw new IllegalStateException("MySQL から申請一覧を取得できませんでした", e);
        }
    }

    @Override
    public Optional<CloudResourceRequest> findById(String requestId) {
        String sql = "SELECT request_id, project_name, resource_type, environment_name, status, requested_by, created_at FROM cloud_resource_requests WHERE request_id = ?";
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, requestId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("MySQL から申請詳細を取得できませんでした", e);
        }
    }

    @Override
    public CloudResourceRequest save(CloudResourceRequest request) {
        String sql = "INSERT INTO cloud_resource_requests (request_id, project_name, resource_type, environment_name, status, requested_by, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, request.requestId);
            statement.setString(2, request.projectName);
            statement.setString(3, request.resourceType);
            statement.setString(4, request.environment);
            statement.setString(5, request.status);
            statement.setString(6, request.requestedBy);
            statement.setTimestamp(7, Timestamp.from(Instant.parse(request.createdAt)));
            statement.executeUpdate();
            return request;
        } catch (SQLException e) {
            throw new IllegalStateException("MySQL へ申請を登録できませんでした", e);
        }
    }

    @Override
    public Optional<CloudResourceRequest> updateStatus(String requestId, String status) {
        String sql = "UPDATE cloud_resource_requests SET status = ? WHERE request_id = ?";
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setString(2, requestId);
            int updated = statement.executeUpdate();
            if (updated == 0) {
                return Optional.empty();
            }
            return findById(requestId);
        } catch (SQLException e) {
            throw new IllegalStateException("MySQL の申請ステータスを更新できませんでした", e);
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    private CloudResourceRequest mapRow(ResultSet resultSet) throws SQLException {
        CloudResourceRequest request = new CloudResourceRequest(
            resultSet.getString("request_id"),
            resultSet.getString("project_name"),
            resultSet.getString("resource_type"),
            resultSet.getString("environment_name"),
            resultSet.getString("status"),
            resultSet.getString("requested_by")
        );
        request.createdAt = resultSet.getTimestamp("created_at").toInstant().toString();
        return request;
    }

    private void waitForDatabase() {
        for (int attempt = 1; attempt <= 60; attempt++) {
            try (Connection connection = openConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM cloud_resource_requests");
                 ResultSet ignored = statement.executeQuery()) {
                return;
            } catch (SQLException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("MySQL 接続待機が中断されました", interruptedException);
                }
            }
        }
        throw new IllegalStateException("MySQL に接続できませんでした: " + jdbcUrl);
    }
}
