package at.sudo200.essentia.command;

import at.sudo200.essentia.service.warp.WarpService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

/**
 * removewarp command.
 * Allows the executing player to delete specified warp (if existent)
 *
 * @author sudo200
 */
public final class CommandRemovewarp {
    /**
     * Instance of {@link WarpService}
     */
    private static final WarpService warpService = Sponge.getServiceManager().provide(WarpService.class).get();

    private CommandRemovewarp() {
    }

    /**
     * @return command
     */
    @Contract(" -> new")
    public static @NotNull CommandSpec get() {
        return CommandSpec.builder()
                .description(Text.of("Removes a warp"))
                .permission("essentia.command.warp.remove")
                .arguments(
                        GenericArguments.onlyOne(
                                GenericArguments.withSuggestions(
                                        GenericArguments.string(Text.of("warpname")),
                                        warpService.getWarpMap().keySet()
                                )
                        )
                )
                .executor((src, args) -> {
                    String name = args.<String>getOne("warpname").get();

                    if (!warpService.removeWarp(name))
                        throw new CommandException(Text.of("Warp does not exist, thus cannot be removed!"));
                    src.sendMessage(Text.of("Warp removed successfully!"));

                    return CommandResult.success();
                })
                .build();
    }
}
