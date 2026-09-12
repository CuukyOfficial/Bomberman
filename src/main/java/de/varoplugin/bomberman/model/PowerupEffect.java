package de.varoplugin.bomberman.model;

public enum PowerupEffect {

    // Allows players to punch to other players with velocity and small damage
    BULLY,
    // Sets players in close range to detonation of their tnt on fire for a short time
    PYRO,
    // Increases the speed of players
    SPEED,
    // Creates a shockwave that pushes all entities away and gives them damage when sneaked and left clicked
    SHOCKWAVE,
    // Freezes all players in close range for a short time when sneaked and left clicked
    FREEZE,
    // Makes the bombs place by player sticky, they will stay where they first collide with a block or entity
    STICKY,
    // Allows player to instantly detonate oldest bomb he placed when sneaked and left clicked
    DETONATOR,
    // Allows player to carry a bomb in his hand and throw it
    CARRY;

    public static PowerupEffect random() {
        return values()[(int) (Math.random() * values().length)];
    }
}
