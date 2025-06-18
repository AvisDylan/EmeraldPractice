/**
 * Created by dylan on 6/18/2025
 */

package com.emeraldnetwork.emeraldPractice.utils.client;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.richpresence.RichPresenceModule;
import com.lunarclient.apollo.module.richpresence.ServerRichPresence;
import com.lunarclient.apollo.player.ApolloPlayer;
import org.bukkit.entity.Player;

import java.util.Optional;

//TODO ADD SERVER TO SERVER MAPPINGS ONCE SERVER IS DONE

@Deprecated
public final class LunarClientUtils{

    private static final RichPresenceModule RICH_PRESENCE_MODULE = Apollo.getModuleManager().getModule(RichPresenceModule.class);
    
    public static void setDiscordRichPresence(Player player, String game, String gameVariant, String gameState, String playerState, String mapName){
        Optional<ApolloPlayer> apolloPlayer = Apollo.getPlayerManager().getPlayer(player.getUniqueId());
        
        apolloPlayer.ifPresent(presentApolloPlayer -> {
            RICH_PRESENCE_MODULE.overrideServerRichPresence(presentApolloPlayer,
                                                            ServerRichPresence.builder()
                                                                                        .gameName(game)
                                                                                        .gameVariantName(gameVariant)
                                                                                        .gameState(gameState)
                                                                                        .playerState(playerState)
                                                                                        .mapName(mapName).build());
        });
    }
    
    public static void resetDiscordRichPresence(Player player){
        Optional<ApolloPlayer> apolloPlayer = Apollo.getPlayerManager().getPlayer(player.getUniqueId());
        
        apolloPlayer.ifPresent(RICH_PRESENCE_MODULE::resetServerRichPresence);
    }
}
