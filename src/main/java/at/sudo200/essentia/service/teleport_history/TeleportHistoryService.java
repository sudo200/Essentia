package at.sudo200.essentia.service.teleport_history;

import at.sudo200.essentia.service.warp.WarpService.LocationData;

import java.util.Map;
import java.util.Optional;

public interface TeleportHistoryService {
    Map<String, LocationData> getHistoryMap();

    Optional<LocationData> getLastPoint(String uuid);

    void setLastPoint(String uuid, LocationData location);

    void clearHistory(String uuid);
}
