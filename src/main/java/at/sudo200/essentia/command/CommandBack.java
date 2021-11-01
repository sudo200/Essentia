package at.sudo200.essentia.command;

import at.sudo200.essentia.service.teleport_history.TeleportHistoryService;
import at.sudo200.essentia.service.warp.WarpService.LocationData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

/**
 * Back command.
 * Teleports the executing player back to his original location.
 *
 * @author sudo200
 */
public final class CommandBack {
    /**
     * Instance of {@link TeleportHistoryService}
     */
    private static final TeleportHistoryService history = Sponge.getServiceManager().provide(TeleportHistoryService.class).get();

    private CommandBack() {
    }

    /**
     * @return command
     */
    @Contract(" -> new")
    public static @NotNull CommandSpec get() {
        return CommandSpec.builder()
                .description(Text.of("Moves you back one teleport. Works with most teleport commands."))
                .permission("essentia.command.back")
                .executor((src, args) -> {
                    if (!(src instanceof Player))
                        throw new CommandException(Text.of("This command can only be used by players!"));

                    Optional<LocationData> location = history.getLastPoint(((Player) src).getUniqueId().toString());
                    if (!location.isPresent())
                        throw new CommandException(Text.of("There is nothing to go back to!"));

                    history.clearHistory(((Player) src).getUniqueId().toString());
                    ((Player) src).setLocation(location.get().location, location.get().world);

                    return CommandResult.success();
                })
                .build();
    }
}
