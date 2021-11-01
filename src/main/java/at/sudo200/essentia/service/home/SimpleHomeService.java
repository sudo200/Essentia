package at.sudo200.essentia.service.home;

import at.sudo200.essentia.data.ServiceStorage;
import at.sudo200.essentia.service.warp.SimpleWarpService;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class SimpleHomeService implements HomeService {
    private final String key = "homes";
    private final ServiceStorage storage;
    private final Map<String, LocationData> homeMap = new HashMap<>();

    public SimpleHomeService(@NotNull ServiceStorage storage) {
        this.storage = storage;
        Optional<SimpleWarpService.MapEntry[]> entries = storage.getObject(key, SimpleWarpService.MapEntry[].class);
        if (entries.isPresent())
            for (SimpleWarpService.MapEntry entry : entries.get())
                homeMap.put(entry.name, entry.location);
    }

    @Override
    public boolean playerHasHome(@NotNull String uuid) {
        return homeMap.containsKey(uuid);
    }

    @Override
    public @NotNull Map<String, LocationData> getWarpMap() {
        return new HashMap<>(homeMap);
    }

    @Override
    public void setWarp(String uuid, LocationData location) {
        homeMap.put(uuid, location);
        storage.saveObject(
                homeMap.entrySet().stream().map(entry -> new SimpleWarpService.MapEntry(entry.getKey(), entry.getValue())).toArray(SimpleWarpService.MapEntry[]::new),
                key,
                SimpleWarpService.MapEntry[].class
        );
    }

    @Override
    public boolean removeWarp(String name) {
        final boolean removed = homeMap.remove(name, homeMap.get(name));
        if (removed)
            storage.saveObject(
                    homeMap.entrySet().stream().map(entry -> new SimpleWarpService.MapEntry(entry.getKey(), entry.getValue())).toArray(SimpleWarpService.MapEntry[]::new),
                    key,
                    SimpleWarpService.MapEntry[].class
            );
        return removed;
    }

    @Override
    public Optional<LocationData> getWarp(String uuid) {
        return homeMap.get(uuid) == null ? Optional.empty() : Optional.of(homeMap.get(uuid));
    }
}
