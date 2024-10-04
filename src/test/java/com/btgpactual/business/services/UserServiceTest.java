package com.btgpactual.business.services;

import com.btgpactual.data.entities.User;
import com.btgpactual.data.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenExistingUser_whenGetUser_thenReturnUser() {
        // Given
        User existingUser = new User();
        existingUser.setId("user123");
        existingUser.setBalance(new BigDecimal("1000.00"));
        when(userRepository.findAll()).thenReturn(Collections.singletonList(existingUser));

        // When
        User result = userService.getUser();

        // Then
        assertNotNull(result);
        assertEquals(existingUser.getId(), result.getId());
        assertEquals(existingUser.getBalance(), result.getBalance());
        verify(userRepository).findAll();
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void givenNoExistingUser_whenGetUser_thenCreateAndReturnNewUser() {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        User newUser = new User();
        newUser.setId("newUser123");
        newUser.setBalance(new BigDecimal("500000.00"));
        newUser.setEmail("viictorestrada@gmail.com");
        newUser.setPhoneNumber("3146871446");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // When
        User result = userService.getUser();

        // Then
        assertNotNull(result);
        assertEquals(newUser.getId(), result.getId());
        assertEquals(new BigDecimal("500000.00"), result.getBalance());
        assertEquals("viictorestrada@gmail.com", result.getEmail());
        assertEquals("3146871446", result.getPhoneNumber());
        verify(userRepository).findAll();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void givenExistingUser_whenUpdateBalance_thenBalanceUpdated() {
        // Given
        User existingUser = new User();
        existingUser.setId("user123");
        existingUser.setBalance(new BigDecimal("1000.00"));
        when(userRepository.findAll()).thenReturn(Collections.singletonList(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BigDecimal amountToAdd = new BigDecimal("500.00");

        // When
        userService.updateBalance(existingUser.getId(), amountToAdd);

        // Then
        verify(userRepository).save(argThat(user ->
                user.getBalance().compareTo(new BigDecimal("1500.00")) == 0
        ));
    }

    @Test
    void givenExistingUser_whenUpdateBalanceWithNegativeAmount_thenBalanceDecreased() {
        // Given
        User existingUser = new User();
        existingUser.setId("user123");
        existingUser.setBalance(new BigDecimal("1000.00"));
        when(userRepository.findAll()).thenReturn(Collections.singletonList(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BigDecimal amountToSubtract = new BigDecimal("-300.00");

        // When
        userService.updateBalance(existingUser.getId(), amountToSubtract);

        // Then
        verify(userRepository).save(argThat(user ->
                user.getBalance().compareTo(new BigDecimal("700.00")) == 0
        ));
    }
}