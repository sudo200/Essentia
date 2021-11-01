package at.sudo200.essentia.command;

import at.sudo200.essentia.service.home.HomeService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

/**
 * sethome command.
 * Sets the home of the executing player at the position he is standing at
 *
 * @author sudo200
 */
public final class CommandSethome {
    /**
     * Instance of {@link HomeService}
     */
    private static final HomeService homeService = Sponge.getServiceManager().provide(HomeService.class).get();

    private CommandSethome() {
    }

    /**
     * @return command
     */
    @Contract(" -> new")
    public static @NotNull CommandSpec get() {
        return CommandSpec.builder()
                .description(Text.of("Sets your personal home where you are standing"))
                .permission("essentia.command.home.set")
                .executor((src, args) -> {
                    if (!(src instanceof Player))
                        throw new CommandException(Text.of("This command can only be used by players!"));

                    homeService.setWarp(
                            ((Player) src).getUniqueId().toString(),
                            new HomeService.LocationData(((Player) src).getLocation().getPosition(), ((Player) src).getWorld().getUniqueId())
                    );
                    src.sendMessage(Text.of("Created home!"));

                    return CommandResult.success();
                })
                .build();
    }
}
