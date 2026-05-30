package com.victorminono.doppelganger;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SkinManager {
    
    /**
     * Obtém o perfil de game do jogador (contém skin info)
     */
    public static GameProfile getGameProfile(Player player) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object gameProfile = handle.getClass().getField("gameProfile").get(handle);
            return (GameProfile) gameProfile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Cria um perfil com a skin de outro jogador
     */
    public static GameProfile createProfileWithSkin(String cloneName, Player originalPlayer) {
        GameProfile originalProfile = getGameProfile(originalPlayer);
        GameProfile newProfile = new GameProfile(UUID.randomUUID(), cloneName);
        
        // Copiar propriedades de skin
        if (originalProfile != null) {
            for (Property property : originalProfile.getProperties().values()) {
                newProfile.getProperties().put(property.getName(), property);
            }
        }
        
        return newProfile;
    }
    
    /**
     * Cria um perfil com skin de um jogador online
     */
    public static GameProfile getProfileWithSkinByName(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            return createProfileWithSkin(playerName, player);
        }
        return null;
    }
}
