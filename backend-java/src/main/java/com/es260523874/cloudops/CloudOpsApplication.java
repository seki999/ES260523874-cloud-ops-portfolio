package com.es260523874.cloudops;

import com.es260523874.cloudops.controller.RequestController;
import com.es260523874.cloudops.repository.CloudResourceRequestRepository;
import com.es260523874.cloudops.repository.MySqlRequestRepository;
import com.es260523874.cloudops.repository.RequestRepository;
import com.es260523874.cloudops.service.RequestService;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

/** ES260523874 クラウド運用ポータル API のエントリポイントです。 */
public class CloudOpsApplication {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getenv().getOrDefault("API_PORT", "8080"));
        RequestController controller = new RequestController(new RequestService(createRepository()));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/health", controller::handleHealth);
        server.createContext("/api/requests", controller::handleRequests);
        server.setExecutor(null);
        server.start();
        System.out.println("ES260523874 Java API started on port " + port);
    }

    private static CloudResourceRequestRepository createRepository() {
        String dbHost = System.getenv("DB_HOST");
        if (dbHost == null || dbHost.isBlank()) {
            System.out.println("DB_HOST is not set. Using in-memory repository for local verification.");
            return new RequestRepository();
        }
        return new MySqlRequestRepository(
            dbHost,
            System.getenv().getOrDefault("DB_PORT", "3306"),
            System.getenv().getOrDefault("DB_NAME", "cloudops"),
            System.getenv().getOrDefault("DB_USER", "cloudops_user"),
            System.getenv().getOrDefault("DB_PASSWORD", "cloudops_password")
        );
    }
}
