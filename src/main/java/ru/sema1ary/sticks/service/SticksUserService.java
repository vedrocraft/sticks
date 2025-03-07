package ru.sema1ary.sticks.service;

import lombok.NonNull;
import ru.sema1ary.sticks.model.SticksUser;
import service.Service;

import java.util.List;
import java.util.Optional;

public interface SticksUserService extends Service {
    SticksUser save(@NonNull SticksUser user);

    void saveAll(@NonNull List<SticksUser> users);

    Optional<SticksUser> findById(@NonNull Long id);

    Optional<SticksUser> findByUsername(@NonNull String username);

    List<SticksUser> findAll();

    SticksUser getUser(@NonNull String username);
}
