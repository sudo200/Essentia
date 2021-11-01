package at.sudo200.essentia.command;

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

/**
 * setwarp command.
 * Creates a warp at the position the executing player is standing at
 *
 * @author sudo200
 */
public final class CommandSetwarp {
    /**
     * Instance of {@link WarpService}
     */
    private static final WarpService warpService = Sponge.getServiceManager().provide(WarpService.class).get();

    private CommandSetwarp() {
    }

    /**
     * @return command
     */
    @Contract(" -> new")
    public static @NotNull CommandSpec get() {
        return CommandSpec.builder()
                .description(Text.of("Creates a warp at your location"))
                .permission("essentia.command.warp.set")
                .arguments(
                        GenericArguments.onlyOne(
                                GenericArguments.string(Text.of("warpname"))
                        )
                )
                .executor((src, args) -> {
                    if (!(src instanceof Player))
                        throw new CommandException(Text.of("This command can only be used by players!"));

                    final String name = args.<String>getOne("warpname").get();
                    warpService.setWarp(name, new WarpService.LocationData(((Player) src).getLocation().getPosition(), ((Player) src).getWorld().getUniqueId()));
                    src.sendMessage(Text.of("Created warp \"" + name + '"'));

                    return CommandResult.success();
                })
                .build();
    }
}
