package com.langchain4j;


import org.springframework.web.client.RestClient;
import org.springframework.core.io.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SimpleChatClient {
    
    private final RestClient restClient;
    
    public SimpleChatClient() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }
    
    /**
     * Hacer pregunta simple con GET
     */
    public void ask(String question) {
        System.out.println("👤 Tú: " + question);
        System.out.print("🤖 AI: ");
        
        // Codificar para URL
        String encoded = URLEncoder.encode(question, StandardCharsets.UTF_8);
        
        
        try {
            // Use exchange() method to get streaming response
            restClient.get()
                    .uri("/chat/v2/ask?q=" + encoded)
                    .exchange((request, response1) -> {
                        try (var inputStream = response1.getBody();
                             var reader = new BufferedReader(new InputStreamReader(inputStream))) {
                            
                            StringBuilder buffer = new StringBuilder();
                            int ch;
                            while ((ch = reader.read()) != -1) {
                                char character = (char) ch;
                                
                                if (character == '\n' || character == '\r') {
                                    String line = buffer.toString();
                                    if (line.equals("[DONE]")) {
                                        System.out.println("\n✅ Completado");
                                        break;
                                    }
                                    if (line.startsWith("Error:")) {
                                        System.err.println("\n❌ " + line);
                                        break;
                                    }
                                    // Solo imprimir si no es línea vacía
                                    if (!line.trim().isEmpty()) {
                                        System.out.println(); // Nueva línea para separar
                                    }
                                    buffer.setLength(0); // Limpiar buffer
                                } else {
                                    buffer.append(character);
                                    System.out.print(character);
                                    System.out.flush();
                                    Thread.sleep(10); // Pausa más pequeña para mejor efecto streaming
                                }
                            }
                            
                            // Procesar último contenido del buffer si existe
                            if (buffer.length() > 0) {
                                String remaining = buffer.toString();
                                if (remaining.equals("[DONE]")) {
                                    System.out.println("\n✅ Completado");
                                } else if (remaining.startsWith("Error:")) {
                                    System.err.println("\n❌ " + remaining);
                                } else {
                                    System.out.print(remaining);
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("\n❌ Error leyendo stream: " + e.getMessage());
                        }
                        return null;
                    });
        } catch (Exception e) {
            System.err.println("\n❌ Error: " + e.getMessage());
        }
    }

     /**
     * Chat interactivo simple
     */
    public void startChat() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("💬 Chat Simple con GET");
        System.out.println("URL: GET /chat/v2/ask?q=tu_pregunta");
        System.out.println("Escribe 'salir' para terminar");
        System.out.println("=".repeat(50));
        
        while (true) {
            System.out.print("\n💬 Pregunta: ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("salir")) {
                System.out.println("👋 ¡Adiós!");
                break;
            }
            
            if (input.isEmpty()) {
                System.out.println("⚠️ Escribe algo...");
                continue;
            }
            
            System.out.println("-".repeat(50));
            ask(input);
            System.out.println("-".repeat(50));
        }
        
        scanner.close();
    }
    
    public static void main(String[] args) {
        SimpleChatClient client = new SimpleChatClient();
        
        System.out.println("🚀 Iniciando cliente de chat...");
        System.out.println("⚠️  Asegúrate de que:");
        System.out.println("   1. El servidor Spring Boot esté ejecutándose en puerto 8080");
        System.out.println("   2. La variable OPENAI_API_KEY esté configurada");
        System.out.println();
        
        // Ejemplo directo
        System.out.println("🧪 TEST: Quiero que me digas sobre bleach");
        client.ask("Quiero que me digas sobre bleach");
        
        System.out.println("\n" + "=".repeat(40));
        
        System.out.println("🧪 TEST: ¿Qué es Java?");
        client.ask("¿Qué es Java?");
        
        System.out.println("\n" + "=".repeat(40));
        
        System.out.println("🧪 TEST: Escribe un poema corto");
        client.ask("Escribe un poema corto");
        
        System.out.println("\n" + "=".repeat(60) + "\n");
        
        // Chat interactivo
        client.startChat();
    }
}
