package giuseppetavella.zero_chiamate.integrations.anthropic;

import giuseppetavella.zero_chiamate.infrastructure.ai.exceptions.AIException;
import giuseppetavella.zero_chiamate.helpers.FileHelper;
import giuseppetavella.zero_chiamate.helpers.PayloadValidationHelper;
import giuseppetavella.zero_chiamate.integrations.anthropic.exceptions.AnthropicAPIException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
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

    private static final String DEFAULT_SYSTEM_PROMPT = "You are a helpful assistant.";

// --- Public API ---

    public String ask(String userPrompt) {
        return ask(userPrompt, DEFAULT_SYSTEM_PROMPT);
    }

    public String ask(String userPrompt, String systemPrompt) {
        Map<String, Object> body = Map.of(
                "model", MODEL,
                "max_tokens", MAX_TOKENS,
                "system", systemPrompt,
                "messages", List.of(
                        Map.of("role", "user", "content", userPrompt)
                )
        );
        return execute(body);
    }

    public String askWithPdf(byte[] pdfBytes, String prompt) {
        return askWithPdf(pdfBytes, prompt, DEFAULT_SYSTEM_PROMPT);
    }

    public String askWithPdf(byte[] pdfBytes, String prompt, String systemPrompt) {
        PayloadValidationHelper.requiredPdf(pdfBytes);

        String base64Pdf = FileHelper.toBase64(pdfBytes);

        Map<String, Object> body = Map.of(
                "model", MODEL,
                "max_tokens", MAX_TOKENS,
                "system", systemPrompt,
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
                                Map.of("type", "text", "text", prompt)
                        )
                ))
        );
        return execute(body);
    }


    private String execute(Map<String, Object> body) {
        try {
            var request = buildRequest(body);

            try (Response response = http.newCall(request).execute()) {

                // OkHttp can return a null body for HEAD requests or certain error cases
                var bodyString = getBodyAsString(response);

                // Extract the first content block's text from the response
                Map<String, Object> parsed = mapper.readValue(bodyString, Map.class);
                
                List<Map<String, Object>> content = (List<Map<String, Object>>) parsed.get("content");
                
                return (String) content.get(0).get("text");
            }

        } catch (Exception ex) {
            throw new AnthropicAPIException("Failed to call Anthropic API. DETAILS: " + ex.getMessage());
        }
    }
    


    public Request buildRequest(Map<String, Object> body) {
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


    private static @NonNull String getBodyAsString(Response response) throws IOException {
        var responseBody = response.body();

        if (responseBody == null) {
            throw new AnthropicAPIException("Response body is null");
        }

        var bodyString = responseBody.string();

        // Non-2xx: surface the raw API error message for debugging
        if (!response.isSuccessful()) {
            throw new AnthropicAPIException("Anthropic API error (HTTP " + response.code() + "): " + bodyString);
        }
        return bodyString;
    }
    
}