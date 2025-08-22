package com.benjamerc.taskmanager.validation;

import com.benjamerc.taskmanager.service.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistsUserIdValidator implements ConstraintValidator<ExistsUserId, Long> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext context) {
        if (userId == null) {
            return true;
        }
        return userService.existsById(userId);
    }
}
