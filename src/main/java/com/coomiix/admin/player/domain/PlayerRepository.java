package com.coomiix.admin.player.domain;

import java.util.Optional;

public interface PlayerRepository {

    public Player save(Player player);
    public Optional<Player> findById(String id);
    public void deleteById(String id);

}
