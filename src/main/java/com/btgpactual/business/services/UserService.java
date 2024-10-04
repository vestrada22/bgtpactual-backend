package com.btgpactual.business.services;

import com.btgpactual.data.entities.User;
import com.btgpactual.data.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("500000");

    public User getUser() {
        return userRepository.findAll().stream().findFirst()
                .orElseGet(this::createInitialUser);
    }

    @Transactional
    public void updateBalance(String userId, BigDecimal amount) {
        User user = getUser();
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }

    private User createInitialUser() {
        User user = new User();
        user.setBalance(INITIAL_BALANCE);
        user.setEmail("viictorestrada@gmail.com");
        user.setPhoneNumber("3146871446");
        return userRepository.save(user);
    }
}
