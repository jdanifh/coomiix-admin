package com.coomiix.admin.player.domain;

public interface PlayerRepository {

    public Player save(Player player);
    public Player findById(String id);
    public void deleteById(String id);
    public Player update(Player player);

}
