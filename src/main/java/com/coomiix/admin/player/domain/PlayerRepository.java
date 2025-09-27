package com.coomiix.admin.player.domain;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlayerRepository {

    public Player save(Player player);
    public Optional<Player> findById(String id);
    public boolean existsById(String id);
    public void deleteById(String id);
    public Page<Player> search(String name, String email, String classType, Pageable pageable);

}
