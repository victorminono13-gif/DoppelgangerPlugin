package com.victorminono.doppelganger;

import org.bukkit.plugin.java.JavaPlugin;

public class DoppelgangerPlugin extends JavaPlugin {
    
    private DoppelgangerManager manager;
    
    @Override
    public void onEnable() {
        getLogger().info("Doppelgänger ativado!");
        
        manager = new DoppelgangerManager(this);
        
        // Registrar comando
        getCommand("doppel").setExecutor(new DoppelCommand(manager));
        
        // Registrar listeners
        getServer().getPluginManager().registerEvents(
            new DoppelgangerListener(manager), this
        );
    }
    
    @Override
    public void onDisable() {
        if (manager != null) {
            manager.removeAllClones();
        }
        getLogger().info("Doppelgänger desativado!");
    }
}
