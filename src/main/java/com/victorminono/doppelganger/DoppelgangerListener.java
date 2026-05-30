package com.victorminono.doppelganger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class DoppelgangerListener implements Listener {
    
    private DoppelgangerManager manager;
    
    public DoppelgangerListener(DoppelgangerManager manager) {
        this.manager = manager;
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Opcionalmente remover clones quando o jogador sai
        // manager.removeAllClones();
    }
}
