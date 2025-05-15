package com.example.test.bank.util;
public class CardNumberMasker {
    public static String mask(String cardNumber) {
        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }
}
