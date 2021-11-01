package at.sudo200.essentia.command;

import at.sudo200.essentia.service.teleport_history.TeleportHistoryService;
import at.sudo200.essentia.service.warp.WarpService;
import at.sudo200.essentia.util.LocationChecker;
import com.flowpowered.math.vector.Vector2i;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

/**
 * spawn command.
 * Teleports specified player (or executing, if non specified) to the spawn point of the default world
 *
 * @author sudo200
 */
public final class CommandSpawn {
    private static final TeleportHistoryService history = Sponge.getServiceManager().provide(TeleportHistoryService.class).get();

    private CommandSpawn() {
    }

    /**
     * @return command
     */
    @Contract(" -> new")
    public static @NotNull CommandSpec get() {
        return CommandSpec.builder()
                .description(Text.of("Teleports players to spawn"))
                .permission("essentia.command.spawn.self")
                .arguments(
                        GenericArguments.onlyOne(
                                GenericArguments.optional(
                                        GenericArguments.requiringPermission(
                                                GenericArguments.player(Text.of("player"))
                                                , "essentia.command.spawn"
                                        )
                                )
                        )
                )
                .executor((src, args) -> {
                    if (!(src instanceof Player) && !args.hasAny("player"))// When not player and no player specified
                        throw new CommandException(Text.of("Player argument is required for non-players!"));

                    final Player player = args.hasAny("player") ? args.<Player>getOne("player").get() : ((Player) src);

                    history.setLastPoint(
                            player.getUniqueId().toString(),
                            new WarpService.LocationData(
                                    player.getLocation().getPosition(),
                                    player.getWorld().getUniqueId()
                            )
                    );
                    player.setLocation(
                            LocationChecker.checkCoords(
                                    player,
                                    Vector2i.from(
                                            Sponge.getServer().getDefaultWorld().get().getSpawnPosition().getX(),
                                            Sponge.getServer().getDefaultWorld().get().getSpawnPosition().getZ()
                                    )
                            ).get(),
                            Sponge.getServer().getDefaultWorld().get().getUniqueId()
                    );

                    return CommandResult.success();
                })
                .build();
    }
}
