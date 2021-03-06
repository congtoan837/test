package com.poly.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.poly.model.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

}
