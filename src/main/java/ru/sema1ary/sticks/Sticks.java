package ru.sema1ary.sticks;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import ormlite.ConnectionSourceUtil;
import ru.sema1ary.sticks.command.SticksCommand;
import ru.sema1ary.sticks.listener.InteractListener;
import ru.sema1ary.sticks.listener.PreJoinListener;
import ru.sema1ary.sticks.model.SticksUser;
import ru.sema1ary.sticks.service.SticksUserService;
import ru.sema1ary.sticks.service.impl.SticksUserServiceImpl;
import ru.vidoskim.bukkit.service.ConfigService;
import ru.vidoskim.bukkit.service.impl.ConfigServiceImpl;
import ru.vidoskim.bukkit.util.LiteCommandUtil;
import service.ServiceManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Sticks extends JavaPlugin {
    private JdbcPooledConnectionSource connectionSource;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ServiceManager.registerService(ConfigService.class, new ConfigServiceImpl(this));

        initConnectionSource();

        ServiceManager.registerService(SticksUserService.class, new SticksUserServiceImpl(getDao(SticksUser.class)));

        new LiteCommandUtil().create("sticks", new SticksCommand(
                ServiceManager.getService(ConfigService.class),
                ServiceManager.getService(SticksUserService.class)
        ));

        getServer().getPluginManager().registerEvents(new PreJoinListener(
                ServiceManager.getService(SticksUserService.class)), this);
        getServer().getPluginManager().registerEvents(new InteractListener(
                ServiceManager.getService(SticksUserService.class)), this);
    }

    @Override
    public void onDisable() {
        ConnectionSourceUtil.closeConnection(true, connectionSource);
    }

    @SneakyThrows
    private void initConnectionSource() {
        if(ServiceManager.getService(ConfigService.class).get("sql-use")) {
            connectionSource = ConnectionSourceUtil.connectSQLDatabase(
                    ServiceManager.getService(ConfigService.class).get("sql-driver"),
                    ServiceManager.getService(ConfigService.class).get("sql-host"),
                    ServiceManager.getService(ConfigService.class).get("sql-database"),
                    ServiceManager.getService(ConfigService.class).get("sql-user"),
                    ServiceManager.getService(ConfigService.class).get("sql-password"),
                    SticksUser.class);
            return;
        }

        Path databaseFilePath = Paths.get("plugins/sticks/database.sqlite");
        if(!Files.exists(databaseFilePath) && !databaseFilePath.toFile().createNewFile()) {
            return;
        }

        connectionSource = ConnectionSourceUtil.connectNoSQLDatabase("sqlite",
                databaseFilePath.toString(), SticksUser.class);
    }

    @SuppressWarnings("all")
    private <D extends Dao<T, ?>, T> D getDao(Class<T> daoClass) {
        return DaoManager.lookupDao(connectionSource, daoClass);
    }
}
