package com.es260523874.cloudops;

import com.es260523874.cloudops.controller.RequestController;
import com.es260523874.cloudops.repository.RequestRepository;
import com.es260523874.cloudops.service.RequestService;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

/** ES260523874 クラウド運用ポータル API のエントリポイントです。 */
public class CloudOpsApplication {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getenv().getOrDefault("API_PORT", "8080"));
        RequestController controller = new RequestController(new RequestService(new RequestRepository()));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/health", controller::handleHealth);
        server.createContext("/api/requests", controller::handleRequests);
        server.setExecutor(null);
        server.start();
        System.out.println("ES260523874 Java API started on port " + port);
    }
}

