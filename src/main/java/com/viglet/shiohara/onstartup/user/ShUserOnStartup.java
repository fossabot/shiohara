package com.viglet.shiohara.onstartup.user;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.user.ShUser;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.user.ShUserRepository;

@Component
public class ShUserOnStartup {

	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;

	public void createDefaultRows() {

		if (shUserRepository.findAll().isEmpty()) {
			ShPostType shPostType = shPostTypeRepository.findByName("PT-TEXT");

			ShUser shUser = new ShUser();

			shUser.setEmail("admin@localhost.local");
			shUser.setFirstName("Admin");
			shUser.setLastLogin(new Date());
			shUser.setLastName("Administrator");
			shUser.setLastPostType(String.valueOf(shPostType.getId()));
			shUser.setLoginTimes(0);
			shUser.setPassword("admin");
			shUser.setRealm("default");
			shUser.setUsername("admin");

			shUserRepository.save(shUser);

		}

	}
}
