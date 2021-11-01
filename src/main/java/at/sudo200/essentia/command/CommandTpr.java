package at.sudo200.essentia.command;

import at.sudo200.essentia.Loader;
import at.sudo200.essentia.config.ConfigManager;
import at.sudo200.essentia.service.teleport_history.TeleportHistoryService;
import at.sudo200.essentia.service.warp.WarpService;
import at.sudo200.essentia.util.LocationChecker;
import com.flowpowered.math.GenericMath;
import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3d;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.DimensionTypes;

import java.util.Optional;

/**
 * tpr command.
 * Teleports the specified player (or executing, if non specified) to a random location in the overworld
 *
 * @author sudo200
 */
public final class CommandTpr {
    private static final TeleportHistoryService history = Sponge.getServiceManager().provide(TeleportHistoryService.class).get();

    private CommandTpr() {
    }

    /**
     * @return command
     */
    @Contract(" -> new")
    public static @NotNull CommandSpec get() {
        return CommandSpec.builder()
                .description(Text.of("Teleports you to a random location"))
                .permission("essentia.command.tpr.self")
                .arguments(
                        GenericArguments.onlyOne(
                                GenericArguments.optional(
                                        GenericArguments.requiringPermission(
                                                GenericArguments.player(Text.of("player"))
                                                , "essentia.command.tpr"
                                        )
                                )
                        )
                )
                .executor((src, args) -> {
                    if (!(src instanceof Player) && !args.hasAny("player"))// When not player and no player specified
                        throw new CommandException(Text.of("Player argument is required for non-players!"));

                    final Player player = args.hasAny("player") ? args.<Player>getOne("player").get() : ((Player) src);

                    if (player.getWorld().getWorldStorage().getWorldProperties().getDimensionType() != DimensionTypes.OVERWORLD)
                        throw new CommandException(Text.of("This command only works in the overworld!"));

                    randomTp(player);

                    return CommandResult.success();
                })
                .build();
    }

    private static void randomTp(@NotNull Player player) {
        Vector2i randomCoords = getRandomCoords(player);

        Optional<Vector3d> coords = LocationChecker.checkCoords(player, randomCoords);
        while (!coords.isPresent()) {
            randomCoords = getRandomCoords(player);
            coords = LocationChecker.checkCoords(player, randomCoords);
        }

        history.setLastPoint(
                player.getUniqueId().toString(),
                new WarpService.LocationData(
                        player.getLocation().getPosition(),
                        player.getWorld().getUniqueId()
                )
        );
        player.setLocation(
                coords.get(),
                player.getWorldUniqueId().get()
        );
    }


    private static Vector2i getRandomCoords(@NotNull Player player) {
        int configRadius = ConfigManager.get().getNode("commands", "tpr", "tpRadius").getInt();
        int tpDiameter = configRadius <= 0
                ? GenericMath.floor(player.getWorld().getWorldBorder().getDiameter()) :
                Math.min(GenericMath.floor(player.getWorld().getWorldBorder().getDiameter()), configRadius * 2);
        int tpRadius = tpDiameter / 2;
        Vector3d wbCenter = player.getWorld().getWorldBorder().getCenter();
        return Vector2i.from(
                (Loader.rand.nextInt(tpDiameter) - tpRadius) + GenericMath.floor(wbCenter.getX()),
                (Loader.rand.nextInt(tpDiameter) - tpRadius) + GenericMath.floor(wbCenter.getZ())
        );
    }
}
