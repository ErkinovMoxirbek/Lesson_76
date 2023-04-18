package com.example.repository;

import com.example.entity.ProfileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer>  {
    @Override
    <S extends ProfileEntity> S save(S entity);

    @Override
    <S extends ProfileEntity> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    Optional<ProfileEntity> findById(Integer integer);

    @Override
    boolean existsById(Integer integer);

    @Override
    Iterable<ProfileEntity> findAll();

    @Override
    Iterable<ProfileEntity> findAllById(Iterable<Integer> integers);

    @Override
    long count();

    @Override
    void deleteById(Integer integer);

    @Override
    void delete(ProfileEntity entity);

    @Override
    void deleteAllById(Iterable<? extends Integer> integers);

    @Override
    void deleteAll(Iterable<? extends ProfileEntity> entities);

    @Override
    void deleteAll();
    Optional<ProfileEntity> findByEmailAndPasswordAndVisible(String email,String password, boolean visible);
}
