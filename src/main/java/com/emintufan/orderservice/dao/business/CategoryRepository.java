package com.emintufan.orderservice.dao.business;

import com.emintufan.orderservice.entities.business.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
