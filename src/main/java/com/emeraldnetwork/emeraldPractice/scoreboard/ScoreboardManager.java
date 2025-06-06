package com.emeraldnetwork.emeraldPractice.scoreboard;

import com.emeraldnetwork.emeraldPractice.match.Match;
import com.emeraldnetwork.emeraldPractice.match.MatchManager;
import com.emeraldnetwork.emeraldPractice.player.PlayerData;
import com.emeraldnetwork.emeraldPractice.queue.QueueManager;
import com.emeraldnetwork.emeraldPractice.utils.TimeFormatUtils;
import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ScoreboardManager{

    public static void updateBoard(PlayerData playerData){
        FastBoard board = playerData.getFastBoard();
        
        switch(playerData.getPlayerState()){
            case SPAWN -> board.updateLines(
                    ChatColor.RESET + "" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-+------------------+-",
                    "",
                    ChatColor.RESET + " " + ChatColor.DARK_GREEN + "Online" + ChatColor.GRAY + ": " + ChatColor.WHITE + Bukkit.getServer().getOnlinePlayers().size(),
                    ChatColor.RESET + " " + ChatColor.DARK_GREEN + "In Game" + ChatColor.GRAY + ": " + ChatColor.WHITE + MatchManager.getPlayersInMatches(),
                    ChatColor.RESET + " " + ChatColor.DARK_GREEN + "In Queue" + ChatColor.GRAY + ": " + ChatColor.WHITE + QueueManager.QUEUE.stream().count(),
                    "",
                    ChatColor.RESET + " " + ChatColor.GRAY + ChatColor.ITALIC + "emerald-network.com",
                    ChatColor.RESET + "" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-+------------------+-"
            );
            case DUEL -> {
                Match match = MatchManager.getPlayerMatch(playerData);
                
                if(match != null){
                    if(match.getKit().isBoxing()){
                        board.updateLines(
                                ChatColor.RESET + "" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-+------------------+-",
                                "",
                                ChatColor.RESET + " " + ChatColor.DARK_GREEN + "Duration" + ChatColor.GRAY + ": " + ChatColor.WHITE + TimeFormatUtils.formateTime(System.currentTimeMillis() - match.getStartTime()),
                                ChatColor.RESET + " " + ChatColor.DARK_GREEN + "Kit" + ChatColor.GRAY + ": " + ChatColor.WHITE + (match.isRanked() ? "Ranked " : "Unranked ") + match.getKit().getDisplayName(),
                                "",
                                ChatColor.RESET + " " + ChatColor.DARK_GREEN + "Your Hits" + ChatColor.GRAY + ": " + ChatColor.WHITE + match.getTeamHits(playerData),
                                ChatColor.RESET + " " + ChatColor.DARK_GREEN + "Their Hits" + ChatColor.GRAY + ": " + ChatColor.WHITE + match.getOtherTeamHits(playerData),
                                "",
                                ChatColor.RESET + " " + ChatColor.GRAY + ChatColor.ITALIC + "emerald-network.com",
                                ChatColor.RESET + "" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-+------------------+-"
                        );
                    }else{
                        board.updateLines(
                                ChatColor.RESET + "" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-+------------------+-",
                                "",
                                ChatColor.RESET + " " + ChatColor.DARK_GREEN + "Duration" + ChatColor.GRAY + ": " + ChatColor.WHITE + TimeFormatUtils.formateTime(System.currentTimeMillis() - match.getStartTime()),
                                ChatColor.RESET + " " + ChatColor.DARK_GREEN + "Kit" + ChatColor.GRAY + ": " + ChatColor.WHITE + (match.isRanked() ? "Ranked " : "Unranked ") + match.getKit().getDisplayName(),
                                "",
                                ChatColor.RESET + " " + ChatColor.DARK_GREEN + "Your Team" + ChatColor.GRAY + ": " + ChatColor.WHITE + match.getTeam(playerData).getAlivePlayers().size() + "/" + match.getTeam(playerData).getPlayers().size(),
                                ChatColor.RESET + " " + ChatColor.DARK_GREEN + "Their Team" + ChatColor.GRAY + ": " + ChatColor.WHITE + match.getOtherTeam(playerData).getAlivePlayers().size() + "/" + match.getOtherTeam(playerData).getPlayers().size(),
                                "",
                                ChatColor.RESET + " " + ChatColor.GRAY + ChatColor.ITALIC + "emerald-network.com",
                                ChatColor.RESET + "" + ChatColor.GRAY + ChatColor.STRIKETHROUGH + "-+------------------+-"
                        );
                    }
                }
            }
        }
    }
}
