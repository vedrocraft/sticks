package ru.sema1ary.sticks.dao.impl;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import lombok.NonNull;
import ru.sema1ary.sticks.dao.SticksUserDao;
import ru.sema1ary.sticks.model.SticksUser;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

public class SticksUserDaoImpl extends BaseDaoImpl<SticksUser, Long> implements SticksUserDao {
    public SticksUserDaoImpl(ConnectionSource connectionSource, Class<SticksUser> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    @Override
    public SticksUser save(@NonNull SticksUser user) throws SQLException {
        createOrUpdate(user);
        return user;
    }

    @Override
    public void saveAll(@NonNull List<SticksUser> users) throws SQLException {
        callBatchTasks((Callable<Void>) () -> {
            for (SticksUser user : users) {
                createOrUpdate(user);
            }
            return null;
        });
    }

    @Override
    public Optional<SticksUser> findById(@NonNull Long id) throws SQLException {
        SticksUser result = queryForId(id);
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<SticksUser> findByUsername(@NonNull String username) throws SQLException {
        QueryBuilder<SticksUser, Long> queryBuilder = queryBuilder();
        Where<SticksUser, Long> where = queryBuilder.where();
        String columnName = "username";

        SelectArg selectArg = new SelectArg(SqlType.STRING, username.toLowerCase());
        where.raw("LOWER(" + columnName + ")" + " = LOWER(?)", selectArg);
        return Optional.ofNullable(queryBuilder.queryForFirst());
    }

    @Override
    public List<SticksUser> findAll() throws SQLException {
        return queryForAll();
    }
}
