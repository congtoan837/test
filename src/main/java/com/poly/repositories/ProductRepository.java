package com.poly.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.model.Product;
import com.poly.model.ProductDTO;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
	
	@Query("select NEW com.poly.model.ProductDTO(a.Id, b.Id, c.Id, a.Name, a.Price, a.Image, a.Description, a.Status, b.Name, a.Quantity, c.Name) from Product as a "
			+ "inner join com.poly.model.Brand as b on a.Brand = b.Id "
			+ "inner join com.poly.model.Category as c on a.Category = c.Id")
	public List<ProductDTO> innerjoin();
	
	@Query("SELECT p FROM Product p WHERE p.Id = :id" )
	public Product getbyId(@Param("id") Integer id);

	@Query("SELECT p FROM Product p WHERE p.Brand = :id" )
	public List<Product> findProductByBrand(@Param("id") Integer id);

	@Query("SELECT count(p) FROM Product p WHERE p.Brand = :id" )
	public int countProductByBrand(@Param("id") Integer id);
}
