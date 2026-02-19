package app;

import core.FormulaEngine;
import modules.Module;

public class MainApp {

    static class BillingApiModule implements Module {

        private com.sun.net.httpserver.HttpServer server;
        private int usage = 0;

        @Override
        public void init() {
            try {
                server = com.sun.net.httpserver.HttpServer.create(
                        new java.net.InetSocketAddress(8081), 0);

                // UUID endpoint
                server.createContext("/uuid", exchange -> {
                    String response = java.util.UUID.randomUUID().toString();
                    exchange.getResponseHeaders().set("Content-Type", "text/plain");
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().flush();
                    exchange.close();
                });

                // Base64 endpoint
                server.createContext("/base64", exchange -> {
                    String query = exchange.getRequestURI().getQuery();
                    String text = (query != null && query.contains("=")) ? query.split("=")[1] : "";
                    String encoded = java.util.Base64.getEncoder().encodeToString(text.getBytes());
                    exchange.getResponseHeaders().set("Content-Type", "text/plain");
                    exchange.sendResponseHeaders(200, encoded.length());
                    exchange.getResponseBody().write(encoded.getBytes());
                    exchange.getResponseBody().flush();
                    exchange.close();
                });

                // SHA-256 Hash endpoint
                server.createContext("/hash", exchange -> {
                    try {
                        String query = exchange.getRequestURI().getQuery();
                        String text = (query != null && query.contains("=")) ? query.split("=")[1] : "";
                        java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
                        byte[] hashBytes = md.digest(text.getBytes());

                        StringBuilder sb = new StringBuilder();
                        for (byte b : hashBytes) sb.append(String.format("%02x", b));
                        String response = sb.toString();

                        exchange.getResponseHeaders().set("Content-Type", "text/plain");
                        exchange.sendResponseHeaders(200, response.length());
                        exchange.getResponseBody().write(response.getBytes());
                        exchange.getResponseBody().flush();
                        exchange.close();

                    } catch (Exception e) {
                        String error = "Hash error";
                        exchange.getResponseHeaders().set("Content-Type", "text/plain");
                        exchange.sendResponseHeaders(500, error.length());
                        exchange.getResponseBody().write(error.getBytes());
                        exchange.getResponseBody().flush();
                        exchange.close();
                    }
                });

                // Slug endpoint
                server.createContext("/slug", exchange -> {
                    String query = exchange.getRequestURI().getQuery();
                    String text = (query != null && query.contains("=")) ? query.split("=")[1] : "";
                    String slug = text.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");

                    exchange.getResponseHeaders().set("Content-Type", "text/plain");
                    exchange.sendResponseHeaders(200, slug.length());
                    exchange.getResponseBody().write(slug.getBytes());
                    exchange.getResponseBody().flush();
                    exchange.close();
                });

                // Email validation endpoint
                server.createContext("/validate-email", exchange -> {
                    String query = exchange.getRequestURI().getQuery();
                    String email = (query != null && query.contains("=")) ? query.split("=")[1] : "";
                    boolean valid = email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
                    String response = valid ? "valid" : "invalid";

                    exchange.getResponseHeaders().set("Content-Type", "text/plain");
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().flush();
                    exchange.close();
                });

                server.setExecutor(null);
                System.out.println("[BillingApiModule] Initialized on port 8081");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void start() {
            server.start();
            System.out.println("[BillingApiModule] Started");
        }

        @Override
        public void stop() {
            server.stop(0);
            System.out.println("[BillingApiModule] Stopped");
        }
    }

    public static void main(String[] args) {
        FormulaEngine engine = new FormulaEngine();
        engine.registerModule(new BillingApiModule());
        engine.init();
        engine.start();
    }
}
