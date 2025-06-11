package com.emeraldnetwork.emeraldPractice.commands;

import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.player.PlayerManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerState;
import com.emeraldnetwork.emeraldPractice.utils.SpawnPointUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor{
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        Player player = (Player) commandSender;
        PlayerData playerData = PlayerManager.getPlayerData(player.getUniqueId());
        
        
        switch(playerData.getPlayerState()){
            case DUEL -> {
                Match match = MatchManager.getPlayerMatch(playerData);
                
                if(match != null){
                    match.onForfeit(playerData);
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have left the game!"));
                }else
                    commandSender.sendMessage(ChatColor.RED + "You are not in a match!");
            }
            case SPECTATING -> {
                Match match = MatchManager.getSpectatorMatch(playerData);
                
                if(match != null){
                    player.setAllowFlight(false);
                    playerData.setPlayerState(PlayerState.SPAWN);
                    PlayerManager.giveSpawnItems(Bukkit.getPlayer(playerData.getUuid()));
                    player.teleport(SpawnPointUtils.getSpawnPoint());
                    match.getSpectators().remove(playerData);
                    
                    match.getPlayers().forEach(playerData1 -> {
                        Player player1 = Bukkit.getPlayer(playerData1.getUuid());
                        
                        player1.showPlayer(player);
                        player1.sendMessage(ChatColor.DARK_GREEN + player.getName() + ChatColor.GRAY + " has stopped spectating!");
                    });
                }else
                    commandSender.sendMessage(ChatColor.RED + "You are not spectating any matches!");
            }
            case FFA -> {
            
            }
            default -> commandSender.sendMessage(ChatColor.RED + "You can't do this command in your state!");
        }
        
        return true;
    }
}
