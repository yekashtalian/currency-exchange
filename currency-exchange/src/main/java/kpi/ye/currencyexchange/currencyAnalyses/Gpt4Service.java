package kpi.ye.currencyexchange.currencyAnalyses;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class Gpt4Service {

    private final String apiKey = "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"; // Використовуйте змінні оточення або конфігураційні файли для вашого ключа

    public String createCompletion(String prompt) throws Exception {
        try {
            System.out.println("Start request");
            OpenAiService openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));

            CompletionRequest completionRequest = CompletionRequest.builder()
                    .prompt(prompt)
                    .model("text-davinci-003")
                    .maxTokens(1000)
                    .echo(true)
                    .user("system")
                    .build();

            System.out.println("Sending request: " + completionRequest);
            CompletionResult completion = openAiService.createCompletion(completionRequest);
            String response = completion.getChoices().get(0).getText();
            System.out.println("Response: " + response);
            return response;
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
            return "Error processing completion request: " + e.getMessage();
        }
    }
}
