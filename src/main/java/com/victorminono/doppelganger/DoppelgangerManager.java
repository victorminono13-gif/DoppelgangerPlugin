package com.victorminono.doppelganger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import java.util.*;

public class DoppelgangerManager {
    
    private Plugin plugin;
    private Map<UUID, DoppelgangerClone> activeClones = new HashMap<>();
    private Map<UUID, PlayerBehaviorProfile> playerProfiles = new HashMap<>();
    
    public DoppelgangerManager(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public DoppelgangerClone createClone(Player player, long durationSeconds) {
        try {
            // Se não existe perfil, criar novo
            if (!playerProfiles.containsKey(player.getUniqueId())) {
                PlayerBehaviorProfile profile = new PlayerBehaviorProfile(player);
                playerProfiles.put(player.getUniqueId(), profile);
            }
            
            PlayerBehaviorProfile profile = playerProfiles.get(player.getUniqueId());
            
            // Criar clone
            DoppelgangerClone clone = new DoppelgangerClone(
                plugin,
                player,
                profile,
                durationSeconds
            );
            
            clone.spawn();
            activeClones.put(clone.getUUID(), clone);
            
            return clone;
        } catch (Exception e) {
            plugin.getLogger().warning("Erro ao criar clone: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public void removeClone(UUID cloneUUID) {
        DoppelgangerClone clone = activeClones.remove(cloneUUID);
        if (clone != null) {
            clone.remove();
        }
    }
    
    public void removeAllClones() {
        List<UUID> toRemove = new ArrayList<>(activeClones.keySet());
        for (UUID uuid : toRemove) {
            removeClone(uuid);
        }
    }
    
    public PlayerBehaviorProfile getProfile(UUID playerUUID) {
        return playerProfiles.get(playerUUID);
    }
}
