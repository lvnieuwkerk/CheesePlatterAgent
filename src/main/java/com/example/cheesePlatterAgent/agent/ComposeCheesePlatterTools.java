package com.example.cheesePlatterAgent.agent;

import com.example.cheesePlatterAgent.client.CheesePlatterService;
import com.example.cheesePlatterAgent.data.CheeseDetails;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ComposeCheesePlatterTools {
    private final CheesePlatterService service;

    public ComposeCheesePlatterTools(CheesePlatterService service) {
        this.service = service;
    }

    @Tool("""
            Set the user after the username provided by the user at the start of the conversation or changed later on.
            """)
    public void setUser(String username) {
        service.setCurrentUserId(username);
    }

    @Tool("""
            Retrieves cheeses of cheese platter.
            """)
    public List<CheeseDetails> getCheesePlatter(Long userId) {
        return service.getCheesePlatter(userId);
    }

    @Tool("""
            Adds a cheese to a cheese platter.
            """)
    public void addCheese(Long userId, String cheeseName, String type, String producer, String description, Long price) {
        service.addCheese(userId, cheeseName, type, producer, description, price);
    }

    @Tool("""
            Removes a cheese from the cheese platter.
            """)
    public void removeCheese(Long userId, String cheeseName) {
        service.removeCheese(userId, cheeseName);
    }
}