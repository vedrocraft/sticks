package ru.sema1ary.sticks.service.impl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.sema1ary.sticks.dao.SticksUserDao;
import ru.sema1ary.sticks.model.SticksUser;
import ru.sema1ary.sticks.service.SticksUserService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SticksUserServiceImpl implements SticksUserService {
    private final SticksUserDao sticksUserDao;


    @Override
    public SticksUser save(@NonNull SticksUser user) {
        try {
            return sticksUserDao.save(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(@NonNull List<SticksUser> users) {
        try {
            sticksUserDao.saveAll(users);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SticksUser> findById(@NonNull Long id) {
        try {
            return sticksUserDao.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SticksUser> findByUsername(@NonNull String username) {
        try {
            return sticksUserDao.findByUsername(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SticksUser> findAll() {
        try {
            return sticksUserDao.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SticksUser getUser(@NonNull String username) {
        return findByUsername(username).orElseGet(() -> save(SticksUser.builder()
                .username(username)
                .isSticksEnabled(true)
                .build()));
    }
}
