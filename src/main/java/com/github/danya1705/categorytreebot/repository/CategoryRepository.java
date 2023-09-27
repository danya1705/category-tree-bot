package com.github.danya1705.categorytreebot.repository;

import com.github.danya1705.categorytreebot.entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Transactional
    void deleteByPathStartsWith(String str);

    Optional<Category> findByName(String name);
}
