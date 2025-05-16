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
    @Mapping(source = "userId", target = "user")
    Card toEntity(CardDto cardDto);

    default User mapUserIdToUser(String userId) {
        if (userId == null) return null;
        return new User(userId);
    }
}