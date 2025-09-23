package com.coomiix.admin.player.domain;

import java.time.LocalDateTime;

import com.coomiix.admin.player.domain.valueobjects.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    private String id;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Player create(String name, String email, String classType) {
        Player player = new Player();
        player.setName(name);
        player.setEmail(new Email(email));
        player.setClassType(classType);
        player.setLevel(1);
        player.setExperiencePoints(0L);
        player.setHealthPoints(100L);
        player.setManaPoints(50L);
        player.setStrength(10L);
        player.setDefense(10L);
        player.setAgility(10L);
        player.setIntelligence(10L);
        player.setMagicResistance(10L);
        return player;
    }
}
