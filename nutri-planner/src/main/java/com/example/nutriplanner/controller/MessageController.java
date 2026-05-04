package com.example.nutriplanner.controller;

import com.example.nutriplanner.dto.MessageDTO;
import com.example.nutriplanner.dto.UserDTO;
import com.example.nutriplanner.exceptions.ApiExceptionResponse;
import com.example.nutriplanner.mapper.MessageMapper;
import com.example.nutriplanner.mapper.UserMapper;
import com.example.nutriplanner.model.Message;
import com.example.nutriplanner.model.User;
import com.example.nutriplanner.service.UserService;
import com.example.nutriplanner.service.impl.MessageServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/messages")
@Tag(name = "Messages", description = "Mesagerie intre useri")
public class MessageController {

    private final MessageServiceImp messageService;
    private final UserService userService;

    public MessageController(MessageServiceImp messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @Operation(summary = "Trimite un mesaj")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mesaj trimis cu succes"),
            @ApiResponse(responseCode = "404", description = "User negasit")})
    @PostMapping("/send")
    public ResponseEntity<MessageDTO> sendMessage(@RequestParam Long senderId, @RequestParam Long receiverId, @RequestParam String content) throws ApiExceptionResponse{
        Message message = messageService.sendMessage(senderId, receiverId, content);
        return ResponseEntity.ok(MessageMapper.toDto(message));
    }

    @Operation(summary = "Obtine conversatia dintre 2 useri")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conversatie returnata cu succes"),
            @ApiResponse(responseCode = "404", description = "User negasit")})
    @GetMapping("/conversation")
    public ResponseEntity<List<MessageDTO>> getConversation(@RequestParam Long userId1, @RequestParam Long userId2) throws ApiExceptionResponse{
        List<MessageDTO> messages = messageService.getConversation(userId1, userId2)
                .stream()
                .map(MessageMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messages);
    }

    @Operation(summary = "Cauta useri dupa nume")
    @ApiResponse(responseCode = "200", description = "Useri gasiti cu succes")
    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String name) {
        List<UserDTO> users = userService.getAllUsers()
                .stream()
                .filter(u -> u.getName().toLowerCase().contains(name.toLowerCase()))
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Obtine cel mai nou mesaj cu toti useri cu care a vorbit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conversatii returnate cu succes"),
            @ApiResponse(responseCode = "404", description = "User negasit")})
    @GetMapping("/recent")
    public ResponseEntity<List<Map<String, Object>>> getRecentConversations(@RequestParam Long userId) throws ApiExceptionResponse {
        User user = userService.getUserById(userId);
        List<Map<String, Object>> recent = new ArrayList<>();
        List<User> allUsers = userService.getAllUsers();

        for (User other : allUsers) {
            if (other.getId().equals(userId)) continue;

            List<Message> conv = messageService.getConversation(userId, other.getId());
            if (!conv.isEmpty()) {
                Message last = conv.get(conv.size() - 1);
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("userId", other.getId());
                item.put("userName", other.getName());
                item.put("lastMessage", last.getContent());
                item.put("timestamp", last.getTimestamp());
                recent.add(item);
            }
        }
        return ResponseEntity.ok(recent);
    }
}