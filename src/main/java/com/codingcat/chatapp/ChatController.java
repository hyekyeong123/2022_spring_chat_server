package com.codingcat.chatapp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatRepository chatRepository;

    @CrossOrigin
    @GetMapping(value="/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> getMsg(@PathVariable String sender, @PathVariable String receiver){
        return chatRepository.mFindBySender(sender, receiver).subscribeOn(Schedulers.boundedElastic());
    }

    @CrossOrigin
    @PostMapping("/chat")
    public Mono<Chat> setMsg(@RequestBody Chat chat){ // Mono는 데이터를 한번만 리턴
        chat.setCreatedAt(LocalDateTime.now());
        return chatRepository.save(chat); // object를 리턴하면 자동으로 JSON 변환(MessageConverter)
    }
}
