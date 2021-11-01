package at.sudo200.essentia.service.home;

import at.sudo200.essentia.service.warp.WarpService;
import org.jetbrains.annotations.NotNull;

/**
 * interface specifying a service for providing homes.
 * Extension of {@link WarpService}
 *
 * @author sudo200
 */
public interface HomeService extends WarpService {
    /**
     * @param uuid Player uuid
     * @return true, if player has a home
     */
    boolean playerHasHome(@NotNull String uuid);
}
