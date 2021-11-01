package at.sudo200.essentia.service.teleport_history;

import at.sudo200.essentia.service.warp.WarpService.LocationData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class SimpleTeleportHistoryService implements TeleportHistoryService {
    private final Map<String, LocationData> histories = new HashMap<>();

    @Override
    public Map<String, LocationData> getHistoryMap() {
        return new HashMap<>(histories);
    }

    @Override
    public Optional<LocationData> getLastPoint(String uuid) {
        return histories.containsKey(uuid) ? Optional.of(histories.get(uuid)) : Optional.empty();
    }

    @Override
    public void setLastPoint(String uuid, LocationData location) {
        histories.put(uuid, location);
    }

    @Override
    public void clearHistory(String uuid) {
        histories.remove(uuid, histories.get(uuid));
    }
}
