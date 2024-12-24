package com.project.ecommercebase.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseService<E, I, R extends JpaRepository<E, I>> {
    R getRepository();

    default boolean delete(I id) {
        R repository = getRepository();
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
