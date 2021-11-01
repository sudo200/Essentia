package at.sudo200.essentia.command.tpa;

import at.sudo200.essentia.service.teleport_history.TeleportHistoryService;
import at.sudo200.essentia.service.warp.WarpService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

/**
 * tpaccept command.
 * Allows player to accept teleport request to him
 *
 * @author sudo200
 */
public final class CommandTpaccept {
    private static final TeleportHistoryService history = Sponge.getServiceManager().provide(TeleportHistoryService.class).get();

    private CommandTpaccept() {
    }

    /**
     * @return command
     */
    @Contract(" -> new")
    public static @NotNull CommandSpec get() {
        return CommandSpec.builder()
                .description(Text.of("Accepts a TPA-Request, if there is one for you"))
                .permission("essentia.command.tpa.accept")
                .executor((src, args) -> {
                    if (!(src instanceof Player))
                        throw new CommandException(Text.of("This command can only be used by players!"));

                    Player[] requests = TpaRequestMap.getKeys((Player) src);

                    if (requests.length == 0)
                        throw new CommandException(Text.of("There are no TPA-Requests for you!"));

                    for (Player player : requests) {
                        history.setLastPoint(
                                player.getUniqueId().toString(),
                                new WarpService.LocationData(
                                        player.getLocation().getPosition(),
                                        player.getWorld().getUniqueId()
                                )
                        );
                        player.setLocationAndRotation(
                                ((Player) src).getLocation(),
                                ((Player) src).getRotation()
                        );
                        TpaRequestMap.removeByKey(player);
                    }

                    return CommandResult.success();
                })
                .build();
    }
}
