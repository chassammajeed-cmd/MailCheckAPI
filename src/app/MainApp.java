package app;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.OutputStream;
import java.util.UUID;
import java.util.Base64;
import java.security.MessageDigest;

public class MainApp {

    public static void main(String[] args) throws Exception {

        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8081"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // UUID
        server.createContext("/uuid", exchange -> {
            String response = UUID.randomUUID().toString();
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        // Email validation
        server.createContext("/validate-email", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String email = (query != null && query.contains("=")) ? query.split("=")[1] : "";
            boolean valid = email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
            String response = valid ? "valid" : "invalid";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.start();
        System.out.println("Server started on port " + port);
    }
}
