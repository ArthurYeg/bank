package com.example.test.bank.util;

import java.lang.annotation.Annotation;

public class ValidExpiry implements Annotation {
    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
