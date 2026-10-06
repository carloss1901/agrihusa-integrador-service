package com.proyecto.integrador.service;

import static org.mockito.Mockito.mock;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

final class TestMocks {
    private TestMocks() { }

    static Pageable page() {
        return PageRequest.of(0, 10);
    }

    static <T> T repository(Class<T> type) {
        return mock(type, invocation -> {
            Class<?> returnType = invocation.getMethod().getReturnType();
            if (Page.class.isAssignableFrom(returnType)) {
                Pageable pageable = Arrays.stream(invocation.getArguments())
                        .filter(Pageable.class::isInstance)
                        .map(Pageable.class::cast)
                        .findFirst().orElse(page());
                return Page.empty(pageable);
            }
            if (returnType == Optional.class) return Optional.empty();
            if (returnType == boolean.class || returnType == Boolean.class) return false;
            return org.mockito.Answers.RETURNS_DEFAULTS.answer(invocation);
        });
    }
}
