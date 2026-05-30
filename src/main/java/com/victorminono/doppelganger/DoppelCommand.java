package com.victorminono.doppelganger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DoppelCommand implements CommandExecutor {
    
    private DoppelgangerManager manager;
    
    public DoppelCommand(DoppelgangerManager manager) {
        this.manager = manager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (args.length == 0) {
            sender.sendMessage("§c/doppel <jogador> [duração em segundos]");
            return false;
        }
        
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage("§cJogador não encontrado!");
            return false;
        }
        
        long duration = 300; // 5 minutos padrão
        if (args.length > 1) {
            try {
                duration = Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cDuração inválida!");
                return false;
            }
        }
        
        DoppelgangerClone clone = manager.createClone(player, duration);
        
        if (clone != null) {
            sender.sendMessage("§a✓ Doppelgänger de " + player.getName() + 
                " criado! (durará " + duration + "s)");
        } else {
            sender.sendMessage("§cErro ao criar Doppelgänger!");
        }
        
        return true;
    }
}
