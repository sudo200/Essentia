package at.sudo200.essentia.command;

import at.sudo200.essentia.service.home.HomeService;
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
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

/**
 * Home command.
 * Teleports the executing player to his home (if he has one)
 *
 * @author sudo200
 */
public final class CommandHome {
    /**
     * Instance of {@link HomeService}
     */
    private static final HomeService homeService = Sponge.getServiceManager().provide(HomeService.class).get();
    /**
     * Instance of {@link TeleportHistoryService}
     */
    private static final TeleportHistoryService history = Sponge.getServiceManager().provide(TeleportHistoryService.class).get();

    private CommandHome() {
    }

    /**
     * @return command
     */
    @Contract(" -> new")
    public static @NotNull CommandSpec get() {
        return CommandSpec.builder()
                .description(Text.of("Teleports you to your personal home"))
                .permission("essentia.command.home.tp")
                .executor((src, args) -> {
                    if (!(src instanceof Player))
                        throw new CommandException(Text.of("This command can only be used by players!"));

                    Optional<WarpService.LocationData> location = homeService.getWarp(((Player) src).getUniqueId().toString());
                    if (!location.isPresent())
                        throw new CommandException(Text.join(
                                Text.of("You don't have a home yet!\nExecute "),
                                Text.builder("/sethome").onClick(TextActions.suggestCommand("/sethome")).color(TextColors.DARK_GREEN).toText(),
                                Text.of(" where you want your home to be!")
                        ));

                    history.setLastPoint(
                            ((Player) src).getUniqueId().toString(),
                            new WarpService.LocationData(
                                    ((Player) src).getLocation().getPosition(),
                                    ((Player) src).getWorld().getUniqueId()
                            )
                    );
                    ((Player) src).setLocation(location.get().location, location.get().world);

                    return CommandResult.success();
                })
                .build();
    }
}
