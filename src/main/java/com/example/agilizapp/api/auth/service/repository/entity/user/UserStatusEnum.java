package com.example.agilizapp.api.auth.service.repository.entity.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatusEnum {
    ACTIVE("Ativo"),
    INACTIVE("Inativo"),
    PENDING_AUTHORIZATION("Autorização Pendente");

    private String description;
}