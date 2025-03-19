package ru.sema1ary.sticks;

import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import ru.sema1ary.sticks.command.SticksCommand;
import ru.sema1ary.sticks.listener.InteractListener;
import ru.sema1ary.sticks.listener.PreJoinListener;
import ru.sema1ary.sticks.model.SticksUser;
import ru.sema1ary.sticks.service.SticksUserService;
import ru.sema1ary.sticks.service.impl.SticksUserServiceImpl;
import ru.sema1ary.vedrocraftapi.BaseCommons;
import ru.sema1ary.vedrocraftapi.command.LiteCommandBuilder;
import ru.sema1ary.vedrocraftapi.ormlite.ConnectionSourceUtil;
import ru.sema1ary.vedrocraftapi.ormlite.DatabaseUtil;
import ru.sema1ary.vedrocraftapi.service.ConfigService;
import ru.sema1ary.vedrocraftapi.service.ServiceManager;
import ru.sema1ary.vedrocraftapi.service.impl.ConfigServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Sticks extends JavaPlugin implements BaseCommons {
    @Override
    public void onEnable() {
        ServiceManager.registerService(ConfigService.class, new ConfigServiceImpl(this));

        DatabaseUtil.initConnectionSource(this, SticksUser.class);

        ServiceManager.registerService(SticksUserService.class, new SticksUserServiceImpl(getDao(SticksUser.class)));

        LiteCommandBuilder.builder()
                .commands(new SticksCommand(
                        getService(ConfigService.class),
                        getService(SticksUserService.class)
                ))
                .build();

        getServer().getPluginManager().registerEvents(new PreJoinListener(
                getService(SticksUserService.class)), this);
        getServer().getPluginManager().registerEvents(new InteractListener(
                getService(SticksUserService.class)), this);
    }

    @Override
    public void onDisable() {
        ConnectionSourceUtil.closeConnection(true);

}
