package at.sudo200.essentia;

import at.sudo200.essentia.command.*;
import at.sudo200.essentia.command.tpa.CommandTpa;
import at.sudo200.essentia.command.tpa.CommandTpaccept;
import at.sudo200.essentia.config.ConfigManager;
import at.sudo200.essentia.data.ServiceStorage;
import at.sudo200.essentia.permission.PermissionSetter;
import at.sudo200.essentia.service.home.HomeService;
import at.sudo200.essentia.service.home.SimpleHomeService;
import at.sudo200.essentia.service.teleport_history.SimpleTeleportHistoryService;
import at.sudo200.essentia.service.teleport_history.TeleportHistoryService;
import at.sudo200.essentia.service.warp.SimpleWarpService;
import at.sudo200.essentia.service.warp.WarpService;
import com.google.gson.Gson;
import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;
import java.util.Random;

/**
 * Main Class
 *
 * @author sudo200
 */
@Plugin(
        id = "essentia",
        name = "Essentia",
        description = "Adds basic commands like /spawn, /tpa, etc.",
        authors = {
                "sudo200"
        },
        url = "https://github.com/sudo200/Essentia"
)
public final class Loader {
    /**
     * Instance of {@link Random}
     */
    public static final Random rand = new Random();
    /**
     * Instance of {@link Gson}
     */
    public static final Gson gson = new Gson();
    /**
     * Instance of {@link Logger}
     */
    @Inject
    private Logger logger;
    /**
     * Path to config directory
     */
    @Inject
    @ConfigDir(sharedRoot = false)
    private Path privateConfigDir;
    private ServiceStorage serviceStorage;

    /**
     * Event listener triggered on {@link org.spongepowered.api.GameState#INITIALIZATION}
     */
    @Listener
    public void onServerInit(GameInitializationEvent event) {
        logger.info("Initializing...");

        // ----------------------------------| Config/Storage |----------------------------------------------
        ConfigManager.get().setConfigDir(
                Sponge.getAssetManager().getAsset(this, "config/default.conf").get().getUrl(),
                privateConfigDir
        );
        serviceStorage = new ServiceStorage(privateConfigDir);
        // --------------------------------------------------------------------------------------------------

        // -----------------------------------| Services |----------------------------------------------------
        Sponge.getServiceManager().setProvider(this, WarpService.class, new SimpleWarpService(serviceStorage));
        Sponge.getServiceManager().setProvider(this, HomeService.class, new SimpleHomeService(serviceStorage));
        Sponge.getServiceManager().setProvider(this, TeleportHistoryService.class, new SimpleTeleportHistoryService());
        // ---------------------------------------------------------------------------------------------------

        //------------------------------------| Event Listeners |---------------------------------------------
        Sponge.getEventManager().registerListeners(this, serviceStorage);
        Sponge.getEventManager().registerListeners(this, ConfigManager.get());
        // ---------------------------------------------------------------------------------------------------

        CommentedConfigurationNode commands = ConfigManager.get().getNode("commands");
        // -----------------------------------| Commands |----------------------------------------------------
        if (commands.getNode("spawn", "enabled").getBoolean())
            Sponge.getCommandManager().register(this, CommandSpawn.get(), "spawn", "s");
        if (commands.getNode("invsee", "enabled").getBoolean())
            Sponge.getCommandManager().register(this, CommandInvsee.get(this), "invsee");
        if (commands.getNode("tpx", "enabled").getBoolean())
            Sponge.getCommandManager().register(this, CommandTpx.get(), "tpx");
        if (commands.getNode("tpa", "enabled").getBoolean()) {
            Sponge.getCommandManager().register(this, CommandTpa.get(), "tpa");
            Sponge.getCommandManager().register(this, CommandTpaccept.get(), "tpaccept");
        }
        if (commands.getNode("tpr", "enabled").getBoolean())
            Sponge.getCommandManager().register(this, CommandTpr.get(), "tpr");
        if (commands.getNode("warp", "warp", "enabled").getBoolean())
            Sponge.getCommandManager().register(this, CommandWarp.get(), "warp", "w");
        if (commands.getNode("warp", "setwarp", "enabled").getBoolean())
            Sponge.getCommandManager().register(this, CommandSetwarp.get(), "setwarp", "sw");
        if (commands.getNode("warp", "removewarp", "enabled").getBoolean())
            Sponge.getCommandManager().register(this, CommandRemovewarp.get(), "removewarp", "rw");
        if (commands.getNode("warp", "warplist", "enabled").getBoolean())
            Sponge.getCommandManager().register(this, CommandWarplist.get(), "warplist", "wl");
        if (commands.getNode("home", "home", "enabled").getBoolean())
            Sponge.getCommandManager().register(this, CommandHome.get(), "home", "h");
        if (commands.getNode("home", "sethome", "enabled").getBoolean())
            Sponge.getCommandManager().register(this, CommandSethome.get(), "sethome", "sh");
        if (commands.getNode("home", "removehome", "enabled").getBoolean())
            Sponge.getCommandManager().register(this, CommandRemovehome.get(), "removehome", "rh");
        if (commands.getNode("tps", "enabled").getBoolean())
            Sponge.getCommandManager().register(this, CommandTps.get(), "tps");
        if (commands.getNode("back", "enabled").getBoolean())
            Sponge.getCommandManager().register(this, CommandBack.get(), "back", "b");
        if (commands.getNode("ping", "enabled").getBoolean())
            Sponge.getCommandManager().register(this, CommandPing.get(), "ping", "p");
        // ----------------------------------------------------------------------------------------------------

        // -----------------------------------| Permissions |--------------------------------------------------
        PermissionSetter.setPerms(this);
        // ----------------------------------------------------------------------------------------------------

        logger.info("Initialization complete!");
    }
}
