package com.victorminono.doppelganger;

import org.bukkit.entity.Player;
import java.util.*;

public class PlayerBehaviorProfile {
    
    private UUID playerUUID;
    private String playerName;
    
    // Comportamento de movimento
    public double avgSpeed = 0.2;
    public double jumpChance = 0.05;
    public double sprintChance = 0.1;
    public double turnSensitivity = 1.0;
    
    // Comportamento de combate
    public double attackSpeed = 0.5;
    public double preferredCombatDistance = 3.0;
    public double reactionTime = 0.3;
    
    // Comportamento geral
    public boolean aggressive = false;
    public boolean intelligent = true;
    public double evasionChance = 0.3;
    
    public PlayerBehaviorProfile(Player player) {
        this.playerUUID = player.getUniqueId();
        this.playerName = player.getName();
        
        // Analisar comportamento do jogador em tempo real
        analyzePlayer(player);
    }
    
    private void analyzePlayer(Player player) {
        // Análise simples baseada em inventário e saúde
        if (player.getHealth() < 10) {
            this.aggressive = false; // Jogador é cauteloso
        }
        
        // Se tem muitas armas, é agressivo
        long weaponCount = Arrays.stream(player.getInventory().getContents())
            .filter(item -> item != null && isWeapon(item.getType().toString()))
            .count();
        
        if (weaponCount > 3) {
            this.aggressive = true;
        }
    }
    
    private boolean isWeapon(String material) {
        return material.contains("SWORD") || material.contains("AXE") || 
               material.contains("PICKAXE") || material.contains("TRIDENT");
    }
}
