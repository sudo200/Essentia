package at.sudo200.essentia.command;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * tps command.
 * Shows the current server tps
 *
 * @author sudo200
 */
public final class CommandTps {
    private CommandTps() {
    }

    /**
     * @return command
     */
    @Contract(" -> new")
    public static @NotNull CommandSpec get() {
        return CommandSpec.builder()
                .description(Text.of("Shows the current server tps"))
                .permission("essentia.command.tps")
                .executor((src, args) -> {
                    double tps = Sponge.getServer().getTicksPerSecond();
                    Text fancyTps = Text.of(
                            tps > 19.0d ? TextColors.GREEN :
                                    tps > 15.0d ? TextColors.YELLOW :
                                            tps >= 1.0d ? TextColors.RED :
                                                    TextColors.DARK_RED,
                            Math.round(tps * 100) / 100d);
                    src.sendMessage(Text.join(Text.of("Current TPS: "), fancyTps));
                    return CommandResult.success();
                })
                .build();
    }
}
