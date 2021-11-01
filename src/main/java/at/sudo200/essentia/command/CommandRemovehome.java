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
 * removehome command.
 * Removes the home of the executing player, if he has one.
 *
 * @author sudo200
 */
public final class CommandRemovehome {
    /**
     * Instance of {@link HomeService}
     */
    private static final HomeService homeService = Sponge.getServiceManager().provide(HomeService.class).get();

    private CommandRemovehome() {
    }

    /**
     * @return command
     */
    @Contract(" -> new")
    public static @NotNull CommandSpec get() {
        return CommandSpec.builder()
                .description(Text.of("Removes your home"))
                .permission("essentia.command.home.remove")
                .executor((src, args) -> {
                    if (!(src instanceof Player))
                        throw new CommandException(Text.of("This command can only be used by players!"));

                    if (!homeService.removeWarp(((Player) src).getUniqueId().toString()))
                        throw new CommandException(Text.of("You don't have a home to remove!"));
                    src.sendMessage(Text.of("Home removed successfully!"));

                    return CommandResult.success();
                })
                .build();
    }
}
