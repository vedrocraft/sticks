package ru.sema1ary.sticks.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.sema1ary.sticks.model.SticksUser;
import ru.sema1ary.sticks.service.SticksUserService;
import ru.vidoskim.bukkit.service.ConfigService;

@RequiredArgsConstructor
@Command(name = "sticks")
public class SticksCommand {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final ConfigService configService;
    private final SticksUserService userService;

    @Async
    @Execute(name = "reload")
    @Permission("sticks.reload")
    void reload (@Context CommandSender sender) {
        configService.reload();
        sender.sendMessage(miniMessage.deserialize(configService.get("reload-message")));
    }

    @Async
    @Execute
    @Permission("sticks.change")
    void change(@Context Player sender) {
        SticksUser user = userService.getUser(sender.getName());

        if(user.isSticksEnabled()) {
            user.setSticksEnabled(false);
            sender.sendMessage(miniMessage.deserialize(configService.get("disabled-sticks-message")));
        } else {
            user.setSticksEnabled(true);
            sender.sendMessage(miniMessage.deserialize(configService.get("enabled-sticks-message")));
        }

        userService.save(user);
    }

    @Async
    @Execute
    @Permission("sticks.change.other")
    void change(@Context CommandSender sender, @Arg("игрок") Player target) {
        SticksUser user = userService.getUser(target.getName());

        if(user.isSticksEnabled()) {
            user.setSticksEnabled(false);
            sender.sendMessage(miniMessage.deserialize(
                    ((String) configService.get("target-disabled-sticks-message")).replace("{player}", target.getName())
            ));
        } else {
            user.setSticksEnabled(true);
            sender.sendMessage(miniMessage.deserialize(
                    ((String) configService.get("target-enabled-sticks-message")).replace("{player}", target.getName())
            ));
        }

        userService.save(user);
    }
}
