package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import com.emeraldnetwork.emeraldPractice.utils.SpawnPointUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        Player player = (Player) commandSender;
        PlayerData playerData = PlayerManager.getPlayerData(player);
        Match match = MatchManager.getPlayerMatch(playerData);
        
        if(match != null){
            PlayerManager.giveSpawnItems(player);
            player.teleport(SpawnPointUtils.getSpawnPoint());
            playerData.setPlayerState(PlayerState.SPAWN);
            match.onLeave(playerData);
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have left the game!"));
        }else
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are not in a match!"));
        return true;
    }
}
