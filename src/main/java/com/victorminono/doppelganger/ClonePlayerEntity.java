package com.victorminono.doppelganger;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class ClonePlayerEntity {
    
    private GameProfile gameProfile;
    private Location location;
    private int entityId;
    private UUID uuid;
    private List<Player> viewers = new ArrayList<>();
    
    private float yaw;
    private float pitch;
    private double x, y, z;
    private double motionX, motionY, motionZ;
    
    public ClonePlayerEntity(Player originalPlayer, Location spawnLocation) {
        this.gameProfile = SkinManager.createProfileWithSkin(
            originalPlayer.getName(), originalPlayer
        );
        this.location = spawnLocation;
        this.uuid = UUID.randomUUID();
        this.entityId = generateEntityId();
        
        this.x = spawnLocation.getX();
        this.y = spawnLocation.getY();
        this.z = spawnLocation.getZ();
        this.yaw = spawnLocation.getYaw();
        this.pitch = spawnLocation.getPitch();
    }
    
    private int generateEntityId() {
        return new Random().nextInt(Integer.MAX_VALUE - 1) + 1;
    }
    
    /**
     * Fazer clone aparecer para um jogador
     */
    public void spawn(Player viewer) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        
        try {
            // 1. Enviar packet de spawn player
            sendPlayerInfoPacket(viewer, manager);
            
            // 2. Enviar packet de entidade spawn
            sendEntitySpawnPacket(viewer, manager);
            
            // 3. Enviar packet de metadados
            sendMetadataPacket(viewer, manager);
            
            // Adicionar à lista de viewers
            if (!viewers.contains(viewer)) {
                viewers.add(viewer);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void sendPlayerInfoPacket(Player viewer, ProtocolManager manager) throws Exception {
        PacketContainer packet = manager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        manager.sendServerPacket(viewer, packet);
    }
    
    /**
     * Enviar packet de spawn da entidade
     */
    public void sendEntitySpawnPacket(Player viewer, ProtocolManager manager) throws Exception {
        PacketContainer spawnPacket = manager.createPacket(
            PacketType.Play.Server.SPAWN_ENTITY
        );
        
        spawnPacket.getIntegers()
            .write(0, entityId);
        spawnPacket.getUUIDs()
            .write(0, uuid);
        
        spawnPacket.getDoubles()
            .write(0, x);
        spawnPacket.getDoubles()
            .write(1, y);
        spawnPacket.getDoubles()
            .write(2, z);
        
        spawnPacket.getBytes()
            .write(0, (byte) (yaw * 256F / 360F));
        spawnPacket.getBytes()
            .write(1, (byte) (pitch * 256F / 360F));
        
        manager.sendServerPacket(viewer, spawnPacket);
    }
    
    /**
     * Enviar metadados
     */
    public void sendMetadataPacket(Player viewer, ProtocolManager manager) throws Exception {
        PacketContainer metadataPacket = manager.createPacket(
            PacketType.Play.Server.ENTITY_METADATA
        );
        
        metadataPacket.getIntegers().write(0, entityId);
        manager.sendServerPacket(viewer, metadataPacket);
    }
    
    /**
     * Mover clone
     */
    public void teleport(Location newLocation) {
        this.x = newLocation.getX();
        this.y = newLocation.getY();
        this.z = newLocation.getZ();
        this.yaw = newLocation.getYaw();
        this.pitch = newLocation.getPitch();
        
        sendTeleportPackets();
    }
    
    private void sendTeleportPackets() {
        try {
            ProtocolManager manager = ProtocolLibrary.getProtocolManager();
            
            for (Player viewer : new ArrayList<>(viewers)) {
                if (!viewer.isOnline()) {
                    viewers.remove(viewer);
                    continue;
                }
                
                PacketContainer teleportPacket = manager.createPacket(
                    PacketType.Play.Server.ENTITY_TELEPORT
                );
                
                teleportPacket.getIntegers().write(0, entityId);
                teleportPacket.getDoubles().write(0, x);
                teleportPacket.getDoubles().write(1, y);
                teleportPacket.getDoubles().write(2, z);
                teleportPacket.getBytes().write(0, (byte) (yaw * 256F / 360F));
                teleportPacket.getBytes().write(1, (byte) (pitch * 256F / 360F));
                
                manager.sendServerPacket(viewer, teleportPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Remover clone da visão dos jogadores
     */
    public void remove() {
        try {
            ProtocolManager manager = ProtocolLibrary.getProtocolManager();
            
            for (Player viewer : new ArrayList<>(viewers)) {
                if (viewer.isOnline()) {
                    PacketContainer destroyPacket = manager.createPacket(
                        PacketType.Play.Server.ENTITY_DESTROY
                    );
                    
                    destroyPacket.getIntegerArrays().write(0, new int[]{entityId});
                    manager.sendServerPacket(viewer, destroyPacket);
                }
            }
            
            viewers.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Location getLocation() {
        return new Location(location.getWorld(), x, y, z, yaw, pitch);
    }
    
    public void setLocation(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public int getEntityId() {
        return entityId;
    }
}
