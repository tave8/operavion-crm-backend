package giuseppetavella.zero_chiamate.integrations.anthropic;

import giuseppetavella.zero_chiamate.infrastructure.ai.exceptions.AIException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.helpers.PayloadValidationHelper;
import giuseppetavella.zero_chiamate.integrations.anthropic.dto.to_send.AnthropicMessageToSendDTO;
import giuseppetavella.zero_chiamate.integrations.anthropic.dto.to_send.AnthropicRequestDTO;
import giuseppetavella.zero_chiamate.integrations.anthropic.dto.sent.AnthropicResponseDTO;
import giuseppetavella.zero_chiamate.integrations.anthropic.exceptions.AnthropicAPIException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AnthropicAPIService {

    @Value("${anthropic.api-key}")
    private String apiKey;

    private final OkHttpClient http = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
                                                    .readTimeout(120, TimeUnit.SECONDS)  // PDFs take longer to process
                                                    .writeTimeout(30, TimeUnit.SECONDS)
                                                    .build();
    
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String ANTHROPIC_URL = "https://api.anthropic.com/v1/messages";
    private static final String ANTHROPIC_VERSION = "2023-06-01";
    private static final String MODEL = "claude-sonnet-4-6";
    private static final int MAX_TOKENS = 1024;

    /**
     * Send a prompt, get a text response.
     */
    public String ask(String prompt) 
    {
            
        var request = buildRequest(prompt);

        try (Response response = http.newCall(request).execute()) {
            
            var body = response.body();
            
            if(response.body() == null) {
                throw new AnthropicAPIException("Response body was null.");
            }
            
            var responseBody = body.string();
            
            if (!response.isSuccessful()) {
                throw new AnthropicAPIException("Non-ok status code from Anthropic API. Response: " + responseBody);
            }

            // deserialize
            var parsed = mapper.readValue(responseBody, AnthropicResponseDTO.class);
            
            return parsed.content().getFirst().text();
            
        } catch (Exception ex) {
            throw new AnthropicAPIException("Failed to call Anthropic API. DETAILS: " + ex.getMessage());
        }
        
    }
    
    
    private Request buildRequest(String prompt)
    {
        AnthropicRequestDTO body = new AnthropicRequestDTO(
                MODEL,
                MAX_TOKENS,
                List.of(new AnthropicMessageToSendDTO("user", prompt))
        );

        return new Request.Builder()
                .url(ANTHROPIC_URL)
                .post(RequestBody.create(
                        MediaType.parse("application/json"),
                        mapper.writeValueAsString(body)
                ))
                .addHeader("x-api-key", apiKey)
                .addHeader("anthropic-version", ANTHROPIC_VERSION)
                .addHeader("content-type", "application/json")
                .build();
    }
    

    /**
     * Send a PDF and a prompt, get a text response.
     */
    public String askWithPdf(String prompt, byte[] pdfBytes) throws AIException 
    {
        
        // require file to be a pdf
        PayloadValidationHelper.requiredPdf(pdfBytes);
        
        
        try {
            String base64Pdf = FileHelper.toBase64(pdfBytes);

            Map<String, Object> body = Map.of(
                    "model", MODEL,
                    "max_tokens", MAX_TOKENS,
                    "messages", List.of(Map.of(
                            "role", "user",
                            "content", List.of(
                                    Map.of(
                                            "type", "document",
                                            "source", Map.of(
                                                    "type", "base64",
                                                    "media_type", "application/pdf",
                                                    "data", base64Pdf
                                            )
                                    ),
                                    Map.of(
                                            "type", "text",
                                            "text", prompt
                                    )
                            )
                    ))
            );

            Request request = new Request.Builder()
                    .url(ANTHROPIC_URL)
                    .post(RequestBody.create(
                            MediaType.parse("application/json"),
                            mapper.writeValueAsString(body)
                    ))
                    .addHeader("x-api-key", apiKey)
                    .addHeader("anthropic-version", ANTHROPIC_VERSION)
                    .addHeader("content-type", "application/json")
                    .build();

            try (Response response = http.newCall(request).execute()) {
                String responseBody = response.body().string();

                if (!response.isSuccessful()) {
                    throw new AIException("Anthropic API error: " + responseBody);
                }
                
                // parse the response body into a map
                Map<String, Object> parsed = mapper.readValue(responseBody, Map.class);
                List<Map<String, Object>> content = (List<Map<String, Object>>) parsed.get("content");
                return (String) content.get(0).get("text");
            }

        } catch (AIException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AIException("Failed to call Anthropic API with PDF. DETAILS: " + ex.getMessage());
        }
    }

}