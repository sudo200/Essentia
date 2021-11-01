package at.sudo200.essentia.command;

import at.sudo200.essentia.service.teleport_history.TeleportHistoryService;
import at.sudo200.essentia.service.warp.WarpService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

/**
 * warp command.
 * Teleports the executing player to the specified warp (if it exists)
 *
 * @author sudo200
 */
public final class CommandWarp {
    /**
     * Instance of {@link WarpService}
     */
    private static final WarpService warpService = Sponge.getServiceManager().provide(WarpService.class).get();
    private static final TeleportHistoryService history = Sponge.getServiceManager().provide(TeleportHistoryService.class).get();

    private CommandWarp() {
    }

    /**
     * @return command
     */
    @Contract(" -> new")
    public static @NotNull CommandSpec get() {
        return CommandSpec.builder()
                .description(Text.of("Teleports you to a warp"))
                .permission("essentia.command.warp.tp")
                .arguments(
                        GenericArguments.onlyOne(
                                GenericArguments.withSuggestions(
                                        GenericArguments.string(Text.of("warpname")),
                                        warpService.getWarpMap().keySet()
                                )
                        ),
                        GenericArguments.onlyOne(
                                GenericArguments.playerOrSource(Text.of("player"))
                        )
                )
                .executor((src, args) -> {
                    if (!(src instanceof Player) && !args.hasAny("player"))
                        throw new CommandException(Text.of("Player argument is required for non-players!"));

                    final String name = args.<String>getOne("warpname").get();
                    final Player player = args.<Player>getOne("player").get();
                    final Optional<WarpService.LocationData> world = warpService.getWarp(name);
                    if (!world.isPresent())
                        throw new CommandException(Text.of("There is no warp called \"" + name + '"'));

                    history.setLastPoint(
                            player.getUniqueId().toString(),
                            new WarpService.LocationData(
                                    player.getLocation().getPosition(),
                                    player.getWorld().getUniqueId()
                            )
                    );
                    player.setLocation(world.get().location, world.get().world);

                    return CommandResult.success();
                })
                .build();
    }
}
