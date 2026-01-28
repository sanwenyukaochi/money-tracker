package com.ledger.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAIModelConfiguration {

    @Bean
    public ChatClient openAIChatClient(OpenAiChatModel chatModel){
        return ChatClient.create(chatModel);
    }

    @Bean
    public ChatClient deepSeekAIChatClient(DeepSeekChatModel chatModel){
        return ChatClient.create(chatModel);
    }

    @Bean
    public ChatClient googleGenAiChatClient(GoogleGenAiChatModel chatModel){
        return ChatClient.create(chatModel);
    }

}
