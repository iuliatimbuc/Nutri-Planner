package com.example.nutriplanner.scheduler;

import com.example.nutriplanner.model.User;
import com.example.nutriplanner.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MealNotificationService {

    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public MealNotificationService(UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void breakfastReminder() {
        sendToAllUsers("Good morning! Don't forget to log your breakfast!");
    }

    @Scheduled(cron = "0 0 11 * * ?")
    public void morningSnackReminder() {
        sendToAllUsers("Morning snack time! Don't forget to log your snack!");
    }

    @Scheduled(cron = "0 0 14 * * ?")
    public void lunchReminder() {sendToAllUsers("Lunchtime! Don't forget to log your meal!");}

    @Scheduled(cron = "0 0 16 * * ?")
    public void afternoonSnackReminder() {
        sendToAllUsers("Afternoon snack time! Don't forget to log your snack!");
    }

    @Scheduled(fixedRate = 30000)
    public void dinnerReminder() {
        sendToAllUsers("Dinner time! Don't forget to log your evening meal!");
    }

    private void sendToAllUsers(String message) {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        for (User user : users) {
            messagingTemplate.convertAndSend("/topic/meal-notification/" + user.getId(), message);
        }
    }
}