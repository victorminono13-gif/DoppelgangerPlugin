package com.victorminono.doppelganger;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class DoppelgangerClone {
    
    private Plugin plugin;
    private Player targetPlayer;
    private PlayerBehaviorProfile profile;
    
    private ClonePlayerEntity cloneEntity;
    private UUID cloneUUID;
    private long durationTicks;
    
    private CloneAIState aiState = CloneAIState.IDLE;
    private Entity targetEntity;
    private Location targetLocation;
    private int tickCounter = 0;
    
    enum CloneAIState {
        IDLE, MOVING, ATTACKING, MINING, FLEEING
    }
    
    public DoppelgangerClone(Plugin plugin, Player player, 
                            PlayerBehaviorProfile profile, long durationSeconds) {
        this.plugin = plugin;
        this.targetPlayer = player;
        this.profile = profile;
        this.durationTicks = durationSeconds * 20;
    }
    
    public void spawn() {
        Location spawnLoc = targetPlayer.getLocation().clone();
        spawnLoc.add(2, 0, 0);
        
        // Criar clone com skin perfeita
        cloneEntity = new ClonePlayerEntity(targetPlayer, spawnLoc);
        
        // Fazer aparecer para todos os jogadores online
        for (Player player : Bukkit.getOnlinePlayers()) {
            cloneEntity.spawn(player);
        }
        
        this.cloneUUID = UUID.randomUUID();
        
        // Iniciar AI loop
        startAILoop();
        
        // Timer para remover
        new BukkitRunnable() {
            @Override
            public void run() {
                remove();
            }
        }.runTaskLater(plugin, durationTicks);
    }
    
    private void startAILoop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (cloneEntity == null) {
                    this.cancel();
                    return;
                }
                
                updateAI();
                tickCounter++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }
    
    private void updateAI() {
        // Seguir jogador
        Location playerLoc = targetPlayer.getLocation();
        double distance = cloneEntity.getLocation().distance(playerLoc);
        
        if (distance > 30) {
            cloneEntity.teleport(playerLoc.clone().add(-3, 0, 0));
        } else if (distance > 5) {
            moveToward(playerLoc);
        } else {
            lookAt(playerLoc.add(0, 1.5, 0));
        }
        
        // Detectar inimigos
        List<Entity> nearbyEnemies = getNearbyEnemies();
        if (nearbyEnemies.size() > 0 && profile.aggressive) {
            engageCombat(nearbyEnemies);
        }
        
        // Comportamentos aleatórios
        if (Math.random() < 0.02) {
            if (Math.random() < profile.jumpChance) {
                // jump();
            }
        }
    }
    
    private List<Entity> getNearbyEnemies() {
        List<Entity> enemies = new ArrayList<>();
        World world = cloneEntity.getLocation().getWorld();
        Location cloneLoc = cloneEntity.getLocation();
        
        if (world == null) return enemies;
        
        for (Entity entity : world.getNearbyEntities(cloneLoc, 16, 16, 16)) {
            if (entity instanceof Monster) {
                enemies.add(entity);
            }
        }
        
        return enemies;
    }
    
    private void engageCombat(List<Entity> enemies) {
        Entity closest = enemies.stream()
            .min(Comparator.comparingDouble(e -> 
                cloneEntity.getLocation().distance(e.getLocation())))
            .orElse(null);
        
        if (closest == null) return;
        
        moveToward(closest.getLocation());
        lookAt(closest.getLocation());
    }
    
    private void moveToward(Location target) {
        Location current = cloneEntity.getLocation();
        Vector direction = target.subtract(current).toVector().normalize();
        
        double newX = current.getX() + direction.getX() * profile.avgSpeed;
        double newY = current.getY();
        double newZ = current.getZ() + direction.getZ() * profile.avgSpeed;
        
        Location newLoc = new Location(current.getWorld(), newX, newY, newZ);
        newLoc.setYaw(current.getYaw());
        newLoc.setPitch(current.getPitch());
        
        cloneEntity.teleport(newLoc);
    }
    
    private void lookAt(Location target) {
        Location current = cloneEntity.getLocation();
        Vector direction = target.subtract(current).toVector();
        
        double distance = direction.length();
        if (distance == 0) return;
        
        double yaw = Math.atan2(direction.getX(), direction.getZ()) * 180 / Math.PI;
        double pitch = -Math.asin(direction.getY() / distance) * 180 / Math.PI;
        
        Location newLoc = current.clone();
        newLoc.setYaw((float) yaw);
        newLoc.setPitch((float) pitch);
        
        cloneEntity.teleport(newLoc);
    }
    
    public void remove() {
        if (cloneEntity != null) {
            cloneEntity.remove();
        }
    }
    
    public UUID getUUID() {
        return cloneUUID;
    }
}
