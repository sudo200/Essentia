package at.sudo200.essentia.service.warp;

import at.sudo200.essentia.data.ServiceStorage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class SimpleWarpService implements WarpService {
    private final String key = "warps";
    private final ServiceStorage storage;
    private final Map<String, WarpService.LocationData> warpMap = new HashMap<>();

    public SimpleWarpService(@NotNull ServiceStorage storage) {
        this.storage = storage;
        Optional<MapEntry[]> entries = storage.getObject(key, MapEntry[].class);
        if (entries.isPresent())
            for (MapEntry entry : entries.get())
                warpMap.put(entry.name, entry.location);
    }

    @Override
    public @NotNull Map<String, WarpService.LocationData> getWarpMap() {
        return new HashMap<>(warpMap);
    }

    @Override
    public void setWarp(String name, WarpService.LocationData location) {
        warpMap.put(name, location);
        storage.saveObject(
                warpMap.entrySet().stream().map(entry -> new MapEntry(entry.getKey(), entry.getValue())).toArray(MapEntry[]::new),
                key,
                MapEntry[].class
        );
    }

    @Override
    public Optional<WarpService.LocationData> getWarp(String name) {
        return warpMap.containsKey(name) ? Optional.of(warpMap.get(name)) : Optional.empty();
    }

    @Override
    public boolean removeWarp(String name) {
        final boolean removed = warpMap.remove(name, warpMap.get(name));
        if (removed)
            storage.saveObject(
                    warpMap.entrySet().stream().map(entry -> new MapEntry(entry.getKey(), entry.getValue())).toArray(MapEntry[]::new),
                    key,
                    MapEntry[].class
            );
        return removed;
    }

    public static class MapEntry {
        public String name;
        public WarpService.LocationData location;

        public MapEntry(String name, WarpService.LocationData location) {
            this.name = name;
            this.location = location;
        }
    }
}
