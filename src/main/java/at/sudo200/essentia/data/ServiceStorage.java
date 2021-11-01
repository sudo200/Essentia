package at.sudo200.essentia.data;

import at.sudo200.essentia.Loader;
import at.sudo200.essentia.config.ConfigManager;
import at.sudo200.essentia.util.TimerTask;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Timer;

/**
 * Stores data for services permanently in a JSON-file
 *
 * @author sudo200
 */
public final class ServiceStorage {
    private final int delay = Math.min(ConfigManager.get().getNode("general", "autosave-delay").getInt(), 60);
    private final JsonObject json;
    private final File dataFile;

    public ServiceStorage(@NotNull Path configDir) {
        if (!configDir.toFile().exists())
            configDir.toFile().mkdirs();

        dataFile = new File(configDir.toFile().getAbsolutePath() + File.separator + "services.json");

        JsonObject temp = null;
        try {
            if (!dataFile.createNewFile()) {
                final FileReader reader = new FileReader(dataFile);
                temp = Loader.gson.fromJson(reader, JsonObject.class);
                reader.close();
            }
        } catch (IOException ignored) {
        }

        json = temp == null ? new JsonObject() : temp;
        periodicSave();
    }

    /**
     * Saves an object behind key.
     * Useful for non-generic classes
     *
     * @param object object to store
     * @param key    key to store behind
     */
    public void saveObject(Object object, @NotNull String key) {
        json.add(key, Loader.gson.toJsonTree(object));
    }

    /**
     * Saves an object behind key with type.
     * Useful for generic classes
     *
     * @param object object to store
     * @param key    key to store behind
     * @param type   type to store
     */
    public void saveObject(Object object, @NotNull String key, Type type) {
        json.add(key, Loader.gson.toJsonTree(object, type));
    }

    /**
     * Gets an object behind key, and tries to fit it into class behind type
     *
     * @param key  key to get object from
     * @param type class to use
     * @param <T>  class type
     * @return stored object
     */
    public <T> Optional<T> getObject(@NotNull String key, Class<T> type) {
        return json.get(key) == null ? Optional.empty() : Optional.of(Loader.gson.fromJson(json.get(key), type));
    }


    private void periodicSave() {
        if (delay <= 0)
            return;
        new Timer(true).schedule(
                new TimerTask(() -> {
                    saveToFile();
                    periodicSave();
                }),
                delay * 60000L
        );
    }

    private void saveToFile() {
        try {
            final FileWriter writer = new FileWriter(dataFile);
            Loader.gson.toJson(json, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Event listener for saving to permanent storage
     */
    @Listener
    public void onServerStopping(GameStoppingServerEvent event) {
        saveToFile();
    }
}
