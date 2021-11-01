package at.sudo200.essentia.service.warp;

import com.flowpowered.math.vector.Vector3d;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * interface specifying a service for providing warps
 *
 * @author sudo200
 */
public interface WarpService {
    /**
     * @return a map with the name as key and the {@link LocationData} as value
     */
    @NotNull Map<String, LocationData> getWarpMap();

    /**
     * Creates a warp
     *
     * @param name     warp name
     * @param location location of the warp
     */
    void setWarp(String name, LocationData location);

    /**
     * Gets warp location by name
     *
     * @param name name to get location by
     * @return warp location or {@link Optional#empty()}, if no warp was found by that name
     */
    Optional<LocationData> getWarp(String name);

    /**
     * Removes warp by name
     *
     * @param name name of warp to delete
     * @return true, if warp was deleted
     */
    boolean removeWarp(String name);

    /**
     * Object containing information for teleporting players
     *
     * @author sudo200
     */
    class LocationData {
        /**
         * location coordinates
         */
        public Vector3d location;
        /**
         * unique world id
         */
        public UUID world;

        public LocationData(@NotNull Vector3d location, @NotNull UUID world) {
            this.location = location;
            this.world = world;
        }
    }
}
