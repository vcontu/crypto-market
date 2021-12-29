package com.endava.internship.cryptomarket.confservice.data;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.endava.internship.cryptomarket.confservice.data.model.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserJdbcRepository implements UserRepository{

    private final EntityManager entityManager;

    private final Session session;

    @Override
    public List<User> getAll() {
        return entityManager.createQuery("SELECT u FROM T_USER u", User.class).getResultList();
    }

    @Override
    public Optional<User> get(String username) {
        return Optional.ofNullable(entityManager.find(User.class, username));
    }

    @Override
    public boolean save(User newUser) {
        session.saveOrUpdate(newUser);
        return true;
    }

    @Override
    @Transactional
    public boolean delete(String username) {
        entityManager.getTransaction().begin();
        int updatedRows = entityManager.createQuery("DELETE FROM T_USER u WHERE u.username = :username")
                .setParameter("username", username)
                .executeUpdate();
        entityManager.getTransaction().commit();
        return updatedRows == 1;
    }

    @Override
    public boolean exists(User user) {
        return nonNull(entityManager.find(User.class, user.getUsername()));
    }
}
