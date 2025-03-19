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
import ru.sema1ary.vedrocraftapi.service.ConfigService;
import ru.sema1ary.vedrocraftapi.service.ServiceManager;
import ru.sema1ary.vedrocraftapi.service.impl.ConfigServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Sticks extends JavaPlugin implements BaseCommons {
    @Override
    public void onEnable() {
        saveDefaultConfig();

        ServiceManager.registerService(ConfigService.class, new ConfigServiceImpl(this));

        initConnectionSource();

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

    @SneakyThrows
    private void initConnectionSource() {
        if(getService(ConfigService.class).get("sql-use")) {
            ConnectionSourceUtil.connectSQL(
                    getService(ConfigService.class).get("sql-host"),
                    getService(ConfigService.class).get("sql-database"),
                    getService(ConfigService.class).get("sql-user"),
                    getService(ConfigService.class).get("sql-password"),
                    SticksUser.class);
            return;
        }

        Path databaseFilePath = Paths.get("plugins/sticks/database.sqlite");
        if(!Files.exists(databaseFilePath) && !databaseFilePath.toFile().createNewFile()) {
            return;
        }

        ConnectionSourceUtil.connectNoSQLDatabase(databaseFilePath.toString(), SticksUser.class);
    }
}
