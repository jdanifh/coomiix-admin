package com.coomiix.admin.player.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.coomiix.admin.player.domain.valueobjects.Email;

import lombok.Data;

@Data
@Document(collection = "players")
public class PlayerDocument {
    @Id
    @Indexed(unique = true)
    private String id = UUID.randomUUID().toString();
    private String name;
    private Email email;
    private String classType;
    private Integer level;
    private Long experiencePoints;
    private Long healthPoints;
    private Long manaPoints;
    private Long strength;
    private Long defense;
    private Long agility;
    private Long intelligence;
    private Long magicResistance;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
