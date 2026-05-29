package giuseppetavella.zero_chiamate.runners.experiments.mixing_ai_prompts;

import giuseppetavella.zero_chiamate.infrastructure.ai.AIService;
import giuseppetavella.zero_chiamate.infrastructure.csv.Csv;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.email_attachment.EmailAttachment;
import giuseppetavella.zero_chiamate.integrations.anthropic.AnthropicAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class Main implements CommandLineRunner {
    
    @Autowired
    private AIService aiService;
    
    @Autowired
    private AnthropicAPIService anthropicAPIService;
    
    @Autowired
    private EmailService emailService;
    

    @Override
    public void run(String... args) throws Exception {
        //
        // record PromptPair(String systemPrompt, String userPrompt) {}
        //
        // List<PromptPair> prompts = List.of(
        //         new PromptPair(
        //                 "You are a sarcastic assistant. Be witty and dry. Never be helpful without a sarcastic remark first.",
        //                 "What's the capital of France?"
        //         ),
        //         new PromptPair(
        //                 "You are a pirate. Speak only in pirate dialect. Use 'arr', 'matey', and nautical terms.",
        //                 "Explain how the internet works."
        //         ),
        //         new PromptPair(
        //                 "You are a CRM assistant. Extract structured data from input. Respond only in JSON. No commentary.",
        //                 "John Smith called today, he's interested in the enterprise plan, budget is 50k."
        //         ),
        //         new PromptPair(
        //                 "You are a strict code reviewer. Be blunt. Flag every issue. No encouragement.",
        //                 "Review this: for(int i=0;i<list.size();i++) { db.save(list.get(i)); }"
        //         ),
        //         new PromptPair(
        //                 "You are a Shakespearean poet. Respond only in iambic pentameter. Never break character.",
        //                 "Tell me the weather is nice today."
        //         ),
        //         new PromptPair(
        //                 "You are a fitness coach. Be motivating and intense. Use short punchy sentences. No excuses accepted.",
        //                 "I'm too tired to work out today."
        //         ),
        //         new PromptPair(
        //                 "You are a legal assistant. Be precise. Always add 'consult a lawyer' disclaimer at the end.",
        //                 "Can my landlord enter my apartment without notice?"
        //         ),
        //         new PromptPair(
        //                 "You are a chef. Speak passionately about food. Always suggest an improvement to any dish mentioned.",
        //                 "I made pasta with tomato sauce last night."
        //         ),
        //         new PromptPair(
        //                 "You are a minimalist. Respond in 10 words or fewer. No exceptions.",
        //                 "Can you explain the theory of relativity?"
        //         ),
        //         new PromptPair(
        //                 "You are a therapist. Respond with empathy. Ask one follow-up question. Never give direct advice.",
        //                 "I feel overwhelmed with work lately."
        //         )
        // );
        //
        // var csv = new Csv(List.of("user_prompt", "system_prompt", "answer", "mixed_prompts"));
        //
        // for(var promptPair : prompts) {
        //    
        //     var userPrompt = promptPair.userPrompt();
        //     var systemPrompt = promptPair.systemPrompt();
        //    
        //     var answerSplit = anthropicAPIService.ask(userPrompt, systemPrompt);
        //    
        //     var answerMixed = anthropicAPIService.ask(userPrompt + " " + systemPrompt);
        //    
        //     csv.addRow(
        //            userPrompt,
        //            systemPrompt,
        //             answerMixed,
        //             "YES"
        //     );
        //
        //     csv.addRow(
        //             "",
        //             "",
        //             answerSplit,
        //             "NO"
        //     );
        //    
        // }
        //
        // emailService.sendEmail(
        //         "giuseppetavella8@gmail.com",
        //         "AI report",
        //         "Your report",
        //         new EmailAttachment(csv, "report")
        // );
        
        
    }

}
