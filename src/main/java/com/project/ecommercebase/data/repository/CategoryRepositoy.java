package com.project.ecommercebase.data.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.ecommercebase.data.entity.Category;
import com.project.ecommercebase.dto.response.CategoryResponse;

public interface CategoryRepositoy extends JpaRepository<Category, UUID> {
    @Query(
            value =
                    "With recursive CategoryTree as ( "
                            + "select id, name, parent_id, visibility from category where parent_id is null "
                            + "union all "
                            + "select c.id, c.name, c.parent_id, c.visibility from category c "
                            + "inner join CategoryTree ct on ct.id = c.parent_id) "
                            + "select new com.project.ecommercebase.dto.response.CategoryResponse(ct.id, ct.name, ct.parent_id, ct.visibility) from CategoryTree ct;",
            nativeQuery = true)
    List<CategoryResponse> findALLByTree();
}
