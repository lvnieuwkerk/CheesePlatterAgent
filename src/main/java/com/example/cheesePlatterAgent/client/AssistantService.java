package com.example.cheesePlatterAgent.client;

import com.example.cheesePlatterAgent.agent.Assistant;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import reactor.core.publisher.Flux;

@BrowserCallable
@AnonymousAllowed
public class AssistantService {
    private final Assistant assistant;

    public AssistantService(Assistant assistant) {
        this.assistant = assistant;
    }

    public Flux<String> chat(String chatId, String userMessage) {
        return assistant.chat(chatId, userMessage);
    }
}
