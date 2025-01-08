package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

// Account mappings

    // register account
    @PostMapping("/register")
    public ResponseEntity registerAccount(@RequestBody Account account) {
        // duplicate username
        String username = account.getUsername();
        Account duplicateAccount = accountService.findAccountByUsername(username);
        if (duplicateAccount != null && username.equals(duplicateAccount.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate username.");
        }
        // password is length 3 or less OR username is blank
        else if (account.getPassword().length() < 4 || username.equals("")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password.");
        } else {
            accountService.addAccount(account);
            return ResponseEntity.status(HttpStatus.OK).body(account);
        }
    }

    // login account
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Account account) {
        Account loginAccount = accountService.findAccountByUsername(account.getUsername());
        if (loginAccount != null && loginAccount.getUsername().equals(account.getUsername()) && loginAccount.getPassword().equals(account.getPassword())) {
            return ResponseEntity.status(HttpStatus.OK).body(loginAccount);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login not successful.");
        }
    }

// Message mappings

    // Add message
    @PostMapping("/messages")
    public ResponseEntity postMessage(@RequestBody Message message) {
        // Text is blank or text is over 255 chars or postedBy does not refer to a real user
        Account account = accountService.findAccountById(message.getPostedBy());
        if (message.getMessageText().equals("") || message.getMessageText().length() > 255 || account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message post not successful.");
        }

        // create and add message to db
        messageService.addMessage(message);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // Get all messages
    @GetMapping("/messages")
    public ResponseEntity getMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

    // get message by message id
    @GetMapping("/messages/{id}")
    public ResponseEntity getMessageById(@PathVariable("id") int id) {
        Message message = messageService.getMessageById(id);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // delete message by message id
    @DeleteMapping("/messages/{id}")
    public ResponseEntity deleteMessageById(@PathVariable("id") int id) {
        Integer rowUpdatedInteger = messageService.deleteMessageById(id);
        return ResponseEntity.status(HttpStatus.OK).body(rowUpdatedInteger);
    }

    // update message by message id
    @PatchMapping("/messages/{id}")
    public ResponseEntity updateMessageById(@RequestBody Message message, @PathVariable("id") int id) {
        // message id does not exist OR message text is blank OR message text is over 255 chars
        Message existingMessage = messageService.getMessageById(id);
        if (existingMessage == null || message.getMessageText().equals("") || message.getMessageText().length() > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Update message not successful.");
        }
        Integer rowUpdatedInteger = messageService.updateMessageById(message, id);
        return ResponseEntity.status(HttpStatus.OK).body(rowUpdatedInteger);
    }

    // get all messages by account id
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity getAllMessagesByAccountId(@PathVariable("accountId") int accountId) {
        List<Message> messages = messageService.getAllMessagesByAccountId(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }
}
