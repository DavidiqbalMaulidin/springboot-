package com.david.dompetkuplus.repository;

import com.david.dompetkuplus.model.Category;
import com.david.dompetkuplus.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByType(TransactionType type);

}