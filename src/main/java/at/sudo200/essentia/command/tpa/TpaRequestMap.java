package at.sudo200.essentia.command.tpa;

import at.sudo200.essentia.util.TimerTask;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.entity.living.player.Player;

import java.util.*;

/**
 * "Map" for teleport requests
 *
 * @author sudo200
 */
public final class TpaRequestMap {
    private static final Set<Map.Entry<Player, Player>> requests = new HashSet<>();

    private TpaRequestMap() {
    }

    /**
     * Like {@link Map#put(Object, Object)}, but with msec-delay until automatic removal.
     *
     * @param key          {@link Player}
     * @param value        {@link Player}
     * @param removalDelay msec delay 'til automatic removal
     */
    public static void putAutoremove(@NotNull Player key, @NotNull Player value, long removalDelay) {
        final Map.Entry<Player, Player> entry = new AbstractMap.SimpleImmutableEntry<Player, Player>(key, value);
        requests.add(entry);
        new Timer(true).schedule(new TimerTask(() -> requests.remove(entry)), removalDelay);
    }

    /**
     * Gets all values behind this key
     *
     * @param key {@link Player}
     * @return players behind key
     */
    public static Player @NotNull [] getValues(Player key) {
        return requests.stream().filter(entry -> entry.getKey().equals(key))
                .map(Map.Entry::getValue).toArray(Player[]::new);
    }

    /**
     * Gets all keys pointing to this value
     *
     * @param value {@link Player}
     * @return players pointing to value
     */
    public static Player @NotNull [] getKeys(Player value) {
        return requests.stream().filter(entry -> entry.getValue().equals(value))
                .map(Map.Entry::getKey).toArray(Player[]::new);
    }

    /**
     * Removes all values behind this key
     *
     * @param key {@link Player}
     * @return removed entries
     */
    public static Map.Entry @NotNull [] removeByKey(Player key) {
        final Map.Entry[] entries = requests.stream().filter(e -> e.getKey().equals(key))
                .toArray(Map.Entry[]::new);
        for (Map.Entry<Player, Player> entry : entries)
            requests.remove(entry);

        return entries;
    }

    /**
     * Removes all keys pointing to this value
     *
     * @param value {@link Player}
     * @return removed entries
     */
    public static Map.Entry @NotNull [] removeByValue(Player value) {
        final Map.Entry[] entries = requests.stream().filter(e -> e.getValue().equals(value))
                .toArray(Map.Entry[]::new);
        for (Map.Entry<Player, Player> entry : entries)
            requests.remove(entry);

        return entries;
    }
}
