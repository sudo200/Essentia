package at.sudo200.essentia.command;

import at.sudo200.essentia.service.warp.WarpService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.Map;

/**
 * warplist command.
 * Lists all available warps.
 *
 * @author sudo200
 */
public final class CommandWarplist {
    private static final WarpService warpService = Sponge.getServiceManager().provide(WarpService.class).get();

    private CommandWarplist() {
    }

    @Contract(" -> new")
    public static @NotNull CommandSpec get() {
        return CommandSpec.builder()
                .description(Text.of("Lists all available warps"))
                .permission("essentia.command.warp.list")
                .executor((src, args) -> {
                    final Map<String, WarpService.LocationData> warps = warpService.getWarpMap();

                    if (warps.size() == 0)
                        throw new CommandException(Text.of("There are no warps on this server!"));

                    final Text listDot = Text.of(TextColors.GOLD, " * ");
                    final Text.Builder builder = Text.builder().append(Text.of(TextColors.LIGHT_PURPLE, "Available Warps:\n"));
                    final boolean[] toggle = {false};
                    warps.keySet().forEach(s -> {
                        builder.append(Text.join(listDot,
                                Text.builder(s + '\n')
                                        .onClick(TextActions.runCommand("/warp " + s))
                                        .onHover(TextActions.showText(Text.of(TextColors.GOLD, "Click to teleport")))
                                        .color(toggle[0] ? TextColors.GREEN : TextColors.DARK_GREEN)
                                        .build()
                        ));
                        toggle[0] = !toggle[0];
                    });

                    src.sendMessage(Text.of(builder.build()));

                    return CommandResult.success();
                })
                .build();
    }
}
