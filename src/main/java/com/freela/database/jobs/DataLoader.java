package com.freela.database.jobs;

import com.freela.database.enums.ApiAction;
import com.freela.database.model.Role;
import com.freela.database.repository.RoleRepository;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.discovery.event.ServiceReadyEvent;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@Singleton
@Requires(notEnv = Environment.TEST)
public class DataLoader implements ApplicationEventListener<ServiceReadyEvent> {
	private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

	@Inject
	RoleRepository roleRepository;

	@Override
	public void onApplicationEvent(ServiceReadyEvent event) {
		log.info("Checking for initial data");
		checkRoles();
	}

	@Override
	public boolean supports(ServiceReadyEvent event) {
		return ApplicationEventListener.super.supports(event);
	}

	private void checkRoles() {
		log.info("checkRoles");

		Boolean adminRoleExists = roleRepository.existsByName("ADMIN");
		if(Boolean.FALSE.equals(adminRoleExists)) {
			log.info("createInitialRoles: creating ADMIN role");
			Role admin = new Role();
			admin.setName("ADMIN");
			admin.setApiActions(Set.of(ApiAction.values()));
			roleRepository.save(admin);
		}

		Boolean defaultUserRoleExists = roleRepository.existsByName("DEFAULT_USER");
		if(Boolean.FALSE.equals(defaultUserRoleExists)) {
			log.info("createInitialRoles: creating DEFAULT_USER role");
			Role defaultUser = new Role();
			defaultUser.setName("DEFAULT_USER");
			defaultUser.setApiActions(Set.of(ApiAction.API_USER_GET_SELF, ApiAction.API_USER_UPDATE_SELF));
			roleRepository.save(defaultUser);
		}
 	}
}
