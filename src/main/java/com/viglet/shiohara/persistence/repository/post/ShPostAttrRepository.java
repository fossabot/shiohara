package com.viglet.shiohara.persistence.repository.post;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;

public interface ShPostAttrRepository extends JpaRepository<ShPostAttr, String> {

	List<ShPostAttr> findAll();

	Set<ShPostAttr> findByShPost(ShPost shPost);
	
	Optional<ShPostAttr> findById(String id);

	@SuppressWarnings("unchecked")
	ShPostAttr save(ShPostAttr shPostAttr);

	@Modifying
	@Query("delete from ShPostAttr pa where pa.id = ?1")
	void delete(String shPostAttrId);
}
