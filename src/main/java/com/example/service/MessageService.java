package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            return optionalMessage.get();
        } else {
            return null;
        }
    }

    public Integer deleteMessageById(int id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            messageRepository.delete(optionalMessage.get());
            return 1;
        } else {
            return null;
        }
    }

    public Integer updateMessageById(Message message, int id) {
        message.setMessageId(id);
        messageRepository.save(message);
        return 1;
    }

    public List<Message> getAllMessagesByAccountId(int accountId) {
        return messageRepository.findByPostedBy(accountId);
    }
}
