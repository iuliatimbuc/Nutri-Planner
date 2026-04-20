package com.example.nutri_planner.service;

import com.example.nutri_planner.model.*;
import com.example.nutri_planner.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Ion Popescu");
        user.setEmail("ion@test.com");
        user.setPassword("parola123");
        user.setAge(21);
        user.setGender(Gender.FEMALE);
        user.setWeight(67);
        user.setHeight(170);
        user.setTargetWeight(62);
        user.setGoal(Goal.LOSE_WEIGHT);
        user.setActivityLevel(ActivityLevel.LIGHTLY_ACTIVE);
    }

    @Test
    void getUserById_WhenUserExists_ReturnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals("Ion Popescu", result.getName());
        assertEquals("ion@test.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_WhenUserNotExists_ThrowsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserById(99L));

        assertTrue(exception.getMessage().contains("99"));
    }

    @Test
    void createUser_WhenEmailIsFree_SavesUser() {
        when(userRepository.existsByEmail("ion@test.com")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals("ion@test.com", result.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void createUser_WhenEmailExists_ThrowsException() {
        when(userRepository.existsByEmail("ion@test.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.createUser(user));

        assertTrue(exception.getMessage().contains("already exists"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void completeProfile_LoseWeight_CalculatesCorrectGoals() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(1L, user);

        assertTrue(result.getDailyCalorieGoal() > 0);
        assertTrue(result.getDailyProteinGoal() > 0);
        assertTrue(result.getDailyCarbsGoal() > 0);
        assertTrue(result.getDailyFatGoal() > 0);

        assertTrue(result.getDailyProteinGoal() > result.getDailyFatGoal());

        System.out.println("Calorii: " + result.getDailyCalorieGoal());
        System.out.println("Proteine: " + result.getDailyProteinGoal() + "g");
        System.out.println("Carbs: " + result.getDailyCarbsGoal() + "g");
        System.out.println("Fat: " + result.getDailyFatGoal() + "g");
    }

    @Test
    void updateUser_LoseWeight_CalculatesCorrectGoals() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(1L, user);

        System.out.println("Calorii: " + result.getDailyCalorieGoal());
        System.out.println("Proteine: " + result.getDailyProteinGoal());
        System.out.println("Carbs: " + result.getDailyCarbsGoal());
        System.out.println("Fat: " + result.getDailyFatGoal());

        assertTrue(result.getDailyCalorieGoal() > 0);
        assertTrue(result.getDailyProteinGoal() > 0);
        assertTrue(result.getDailyCarbsGoal() > 0);
        assertTrue(result.getDailyFatGoal() > 0);
        assertTrue(result.getDailyProteinGoal() > result.getDailyFatGoal());
    }
    @Test
    void deleteUser_WhenUserExists_DeletesSuccessfully() {
        when(userRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_WhenUserNotExists_ThrowsException() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.deleteUser(99L));
        verify(userRepository, never()).deleteById(any());
    }
}