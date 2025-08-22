package com.benjamerc.taskmanager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistsUserIdValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsUserId {
    String message() default "User with given id does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
