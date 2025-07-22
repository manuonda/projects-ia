package com.langchain4j;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;



@RestController
@RequestMapping("/chat/v2")
public class StreamingChat {

  @Autowired
  private StreamingChatModel streamingChatModel;

  @PostMapping(value = "/stream", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<StreamingResponseBody> basicStream(@RequestBody ChatDTO request) {
    StreamingResponseBody stream = outputStream -> {
      try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream))) {

        streamingChatModel.chat(request.message(), new StreamingChatResponseHandler() {

          @Override
          public void onPartialResponse(String partialResponse) {
            // Enviamos cada token al frontend
            writer.println(partialResponse);
            writer.flush();
          }

          @Override
          public void onCompleteResponse(ChatResponse completeResponse) {
            writer.print("\nDONE");
            writer.flush();
          }

          @Override
          public void onError(Throwable error) {
            writer.print("\nError: " + error.getMessage());
            writer.flush();
          }

        });
      } catch (Exception e) {
        e.printStackTrace();
      }
    };
    return ResponseEntity.ok().body(stream);
  }

  @GetMapping(value = "/ask", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<StreamingResponseBody> askSimple(@RequestParam String q) {
    System.out.println("=== INICIO askSimple con pregunta: " + q);
    
    StreamingResponseBody stream = outputStream -> {
      System.out.println("=== StreamingResponseBody lambda ejecutándose");
      try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream))) {
        
        System.out.println("=== PrintWriter creado correctamente");
        
        if (streamingChatModel == null) {
          System.out.println("=== ERROR: streamingChatModel es null");
          writer.print("Error: StreamingChatModel no está configurado correctamente");
          writer.flush();
          return;
        }
        
        System.out.println("=== streamingChatModel disponible, iniciando chat...");
        
        // Primer escribir algo inmediato para probar el stream
        writer.print("Procesando tu pregunta: " + q + "\n\n");
        writer.flush();
        
        streamingChatModel.chat(q, new StreamingChatResponseHandler() {
          @Override
          public void onPartialResponse(String partialResponse) {
            System.out.println("=== Partial response: " + partialResponse);
            writer.print(partialResponse);
            writer.flush();
          }

          @Override
          public void onCompleteResponse(ChatResponse completeResponse) {
            System.out.println("=== Respuesta completa recibida");
            writer.print("\n[DONE]");
            writer.flush();
          }

          @Override
          public void onError(Throwable error) {
            System.out.println("=== Error en streaming: " + error.getMessage());
            error.printStackTrace();
            writer.print("\nError: " + error.getMessage());
            writer.flush();
          }
        });
      } catch (Exception e) {
        System.err.println("=== Error general en askSimple: " + e.getMessage());
        e.printStackTrace();
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream))) {
          writer.print("Error interno del servidor: " + e.getMessage());
          writer.flush();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    };
    
    System.out.println("=== Retornando ResponseEntity con stream");
    return ResponseEntity.ok().body(stream);
  }
  
  // Endpoint simple para debugging
  @GetMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
  public String testSimple(@RequestParam String q) {
    System.out.println("=== Test endpoint simple llamado con: " + q);
    return "Test response: " + q + " - StreamingModel null? " + (streamingChatModel == null);
  }

}
