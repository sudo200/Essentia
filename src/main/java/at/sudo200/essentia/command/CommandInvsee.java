package at.sudo200.essentia.command;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Carrier;
import org.spongepowered.api.item.inventory.type.CarriedInventory;
import org.spongepowered.api.text.Text;

/**
 * invsee command.
 * Allows the executing player to access the inventory of the specified player
 *
 * @author sudo200
 */
public final class CommandInvsee {
    private CommandInvsee() {
    }

    /**
     * @param plugin plugin instance
     * @return command
     */
    @Contract("_ -> new")
    public static @NotNull CommandSpec get(Object plugin) {
        return CommandSpec.builder()
                .description(Text.of("Allows you to access another player's inventory"))
                .permission("essentia.command.invsee")
                .arguments(
                        GenericArguments.onlyOne(
                                GenericArguments.player(Text.of("player"))
                        )
                )
                .executor((src, args) -> {
                    if (!(src instanceof Player))
                        throw new CommandException(Text.of("This command can only be used by players!"));

                    Player target = args.<Player>getOne("player").get();
                    CarriedInventory<? extends Carrier> inventory = target.getInventory();

                    ((Player) src).openInventory(inventory.root(), Text.of(target.getName() + "'s Inventory"));

                    return CommandResult.success();
                })
                .build();
    }
}
