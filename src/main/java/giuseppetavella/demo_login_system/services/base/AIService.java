package giuseppetavella.demo_login_system.services.base;

import giuseppetavella.demo_login_system.exceptions.AIException;
import giuseppetavella.demo_login_system.helpers.FileHelper;
import giuseppetavella.demo_login_system.helpers.PayloadValidationHelper;
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
public class AIService {

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
    public String ask(String prompt) throws AIException {
        try {
            Map<String, Object> body = Map.of(
                    "model", MODEL,
                    "max_tokens", MAX_TOKENS,
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)
                    )
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
                    throw new AIException("While calling Anthropic API, got non-ok status code. Response body: " + responseBody);
                }

                Map<String, Object> parsed = mapper.readValue(responseBody, Map.class);
                List<Map<String, Object>> content = (List<Map<String, Object>>) parsed.get("content");

                return (String) content.get(0).get("text");
            }

        } catch (AIException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AIException("Failed to call Anthropic API. DETAILS: " + ex.getMessage());
        }
    }


    /**
     * Send a PDF and a prompt, get a text response.
     */
    public String askWithPdf(byte[] pdfBytes, String prompt) throws AIException 
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