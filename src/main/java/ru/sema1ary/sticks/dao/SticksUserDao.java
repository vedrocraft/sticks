package ru.sema1ary.sticks.dao;

import com.j256.ormlite.dao.Dao;
import lombok.NonNull;
import ru.sema1ary.sticks.model.SticksUser;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface SticksUserDao extends Dao<SticksUser, Long> {
    SticksUser save(@NonNull SticksUser user) throws SQLException;

    void saveAll(@NonNull List<SticksUser> users) throws SQLException;

    Optional<SticksUser> findById(@NonNull Long id) throws SQLException;

    Optional<SticksUser> findByUsername(@NonNull String username) throws SQLException;

    List<SticksUser> findAll() throws SQLException;
}
