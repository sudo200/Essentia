package at.sudo200.essentia.config;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

public final class ConfigManager {
    private static final ConfigManager instance = new ConfigManager();

    private ConfigurationLoader<CommentedConfigurationNode> loader = null;
    private CommentedConfigurationNode rootNode = null;
    private URL defaultConfig;


    private ConfigManager() {
    }

    public static ConfigManager get() {
        return instance;
    }

    public void setConfigDir(@NotNull URL defaultConfig, @NotNull Path configDir) {
        this.defaultConfig = defaultConfig;
        loader = HoconConfigurationLoader.builder()
                .setFile(new File(configDir.toFile().getAbsolutePath() + File.separator + "essentia.conf"))
                .build();
        loadConfig();
    }

    private void loadConfig() {
        try {
            rootNode = loader.load();
            rootNode.mergeValuesFrom(
                    HoconConfigurationLoader.builder()
                            .setURL(defaultConfig)
                            .build()
                            .load()
            );
            loader.save(rootNode);
        } catch (IOException ignored) {
        }
    }

    public ConfigurationLoader<CommentedConfigurationNode> getLoader() {
        return loader;
    }

    public @NotNull CommentedConfigurationNode getNode(Object... path) {
        return rootNode.getNode("essentia").getNode(path);
    }

    @Listener
    public void onPluginReload(GameReloadEvent event) {
        loadConfig();
    }
}
