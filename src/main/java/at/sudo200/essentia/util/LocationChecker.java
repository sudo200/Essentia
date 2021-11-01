package at.sudo200.essentia.util;

import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3d;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;

/**
 * Provides methods for getting the y-coordinate of the next safe block by x and z.
 *
 * @author sudo200
 */
public final class LocationChecker {
    private LocationChecker() {
    }

    public static Optional<Vector3d> checkCoords(@NotNull Player player, @NotNull Vector2i xz) {
        int y = player.getWorld().getBlockMax().getY();// Pretty inefficient; Please optimize!
        BlockType blockType;
        do {
            blockType = player.getWorld().getBlockType(xz.getX(), y, xz.getY());

            if (blockType.equals(BlockTypes.WATER) || blockType.equals(BlockTypes.LAVA))
                return Optional.empty();
            y--;
        } while (blockType.equals(BlockTypes.AIR) && y > 0);

        if (y <= 0)
            return Optional.empty();

        return Optional.of(Vector3d.from(xz.getX() + .5, y + 2, xz.getY() + .5));
    }
}
