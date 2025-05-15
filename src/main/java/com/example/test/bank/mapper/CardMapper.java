package com.example.test.bank.mapper;

import com.example.test.bank.dto.CardDto;
import com.example.test.bank.dto.CardResponseDto;
import com.example.test.bank.model.Card;
import com.example.test.bank.model.User;
import com.example.test.bank.util.CardNumberMasker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CardMapper {


    @Mapping(target = "cardNumber", source = "cardNumber", qualifiedByName = "maskCardNumber")
    @Mapping(target = "cardHolderName", expression = "java(mapUserToFullName(card.getUser()))")
    CardDto toDto(Card card);

    @Named("maskCardNumber")
    static String maskCardNumber(String cardNumber) {
        return CardNumberMasker.mask(cardNumber);
    }

    default String mapUserToFullName(User user) {
        return user != null ? user.getFirstName() + " " + user.getLastName() : null;
    }

    CardResponseDto toResponse(Card card);
}
