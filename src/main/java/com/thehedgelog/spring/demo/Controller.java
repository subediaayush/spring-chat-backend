package com.thehedgelog.spring.demo;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api")
@CrossOrigin("*")
public class Controller {

    private final ChatService chatService;

    public Controller(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/user/create")
    public String createUser() {
        return Uuids.timeBased().toString();
    }

    @GetMapping("/message/get")
    public List<ChatMessage> getMessages() {
        return Collections.emptyList();
    }

//    @PostMapping("/message/send")
//    public String sendMessage(@RequestBody ChatMessage message) {
//        chatService.broadcastMessage(message);
//        return "ok";
//    }

}
