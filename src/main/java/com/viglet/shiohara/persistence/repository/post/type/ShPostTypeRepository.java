package com.viglet.shiohara.persistence.repository.post.type;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.post.type.ShPostType;

public interface ShPostTypeRepository extends JpaRepository<ShPostType, String> {

	List<ShPostType> findAll();

	Optional<ShPostType> findById(String id);
	
	ShPostType findByName(String name);

	@SuppressWarnings("unchecked")
	ShPostType save(ShPostType shPostType);

	void delete(ShPostType shPostType);
	
	@Modifying
	@Query("delete from ShPostType pt where pt.id = ?1")
	void delete(String shPostTypeId);
}
