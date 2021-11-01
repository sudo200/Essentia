package at.sudo200.essentia.command;

import at.sudo200.essentia.service.teleport_history.TeleportHistoryService;
import at.sudo200.essentia.service.warp.WarpService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.storage.WorldProperties;

/**
 * tpx command.
 * Teleports specified player (or executing, if non specified) into specified dimension
 *
 * @author sudo200
 */
public final class CommandTpx {
    private static final TeleportHistoryService history = Sponge.getServiceManager().provide(TeleportHistoryService.class).get();

    private CommandTpx() {
    }

    @Contract(" -> new")
    public static @NotNull CommandSpec get() {
        return CommandSpec.builder()
                .description(Text.of("Allows you to change dimension"))
                .permission("essentia.command.tpx")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.dimension(Text.of("dimension"))),
                        GenericArguments.onlyOne(GenericArguments.playerOrSource(Text.of("player")))
                )
                .executor((src, args) -> {
                    final Player player = args.<Player>getOne("player").get();
                    final DimensionType dimensionType = args.<DimensionType>getOne("dimension").get();

                    final WorldProperties world = Sponge.getServer().getAllWorldProperties().stream()
                            .filter(worldProperties -> worldProperties.getDimensionType().equals(dimensionType))
                            .findFirst().get();

                    history.setLastPoint(
                            player.getUniqueId().toString(),
                            new WarpService.LocationData(
                                    player.getLocation().getPosition(),
                                    player.getWorld().getUniqueId()
                            )
                    );
                    player.setLocation(Sponge.getServer().loadWorld(world).get().getSpawnLocation());

                    return CommandResult.success();
                })
                .build();
    }
}
