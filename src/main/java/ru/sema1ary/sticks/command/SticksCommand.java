package ru.sema1ary.sticks.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.async.Async;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.sema1ary.sticks.model.SticksUser;
import ru.sema1ary.sticks.service.SticksUserService;
import ru.sema1ary.vedrocraftapi.player.PlayerUtil;
import ru.sema1ary.vedrocraftapi.service.ConfigService;

@RequiredArgsConstructor
@Command(name = "sticks")
public class SticksCommand {
    private final ConfigService configService;
    private final SticksUserService userService;

    @Async
    @Execute(name = "reload")
    @Permission("sticks.reload")
    void reload (@Context CommandSender sender) {
        configService.reload();
        PlayerUtil.sendMessage(sender, (String) configService.get("reload-message"));
    }

    @Async
    @Execute
    void change(@Context Player sender) {
        SticksUser user = userService.getUser(sender.getName());

        if(user.isSticksEnabled()) {
            user.setSticksEnabled(false);
            PlayerUtil.sendMessage(sender, (String) configService.get("disabled-sticks-message"));
        } else {
            user.setSticksEnabled(true);
            PlayerUtil.sendMessage(sender, (String) configService.get("enabled-sticks-message"));
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

            PlayerUtil.sendMessage(sender,
                    ((String) configService.get("enabled-sticks-message")).replace("{player}", target.getName()
            ));
        } else {
            user.setSticksEnabled(true);
            PlayerUtil.sendMessage(sender,
                    ((String) configService.get("target-enabled-sticks-message")).replace("{player}", target.getName()
            ));
        }

        userService.save(user);
    }
}
