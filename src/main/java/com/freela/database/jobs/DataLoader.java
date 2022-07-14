package com.freela.database.jobs;

import com.freela.database.enums.ApiAction;
import com.freela.database.model.ApiUser;
import com.freela.database.model.Role;
import com.freela.database.repository.ApiUserRepository;
import com.freela.database.repository.RoleRepository;
import com.freela.utils.PasswordUtils;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.discovery.event.ServiceReadyEvent;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.Set;

@Singleton
@Requires(notEnv = Environment.TEST)
public class DataLoader implements ApplicationEventListener<ServiceReadyEvent> {
	private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

	@Value("${com.freela.service.api-user.default-admin-password:admin}")
	private String DEFAULT_ADMIN_PASSWORD;

	@Inject
	RoleRepository roleRepository;

	@Inject
	ApiUserRepository apiUserRepository;

	@Inject
	PasswordUtils passwordUtils;

	@Override
	public void onApplicationEvent(ServiceReadyEvent event) {
		log.info("Checking for initial data");
		try {
			checkRoles();
		} catch (InvalidKeySpecException e) {
			log.error("Failed to run initial data checks", e);
		}
	}

	@Override
	public boolean supports(ServiceReadyEvent event) {
		return ApplicationEventListener.super.supports(event);
	}

	private void checkRoles() throws InvalidKeySpecException {
		log.info("checkRoles");

		Boolean adminRoleExists = roleRepository.existsByName("ADMIN");
		if(Boolean.FALSE.equals(adminRoleExists)) {
			log.info("createInitialRoles: creating ADMIN role");

			// Create ADMIN role
			Role adminRole = new Role();
			adminRole.setName("ADMIN");
			adminRole.setApiActions(Set.of(ApiAction.values()));
			adminRole = roleRepository.save(adminRole);

			// Create ADMIN user
			ApiUser adminApiUser = new ApiUser();
			adminApiUser.setName("Admin");
			adminApiUser.setEmail("admin@admin.com");
			adminApiUser.setPhoneNumber("000000000");
			adminApiUser.setBirthDate(LocalDate.now());
			adminApiUser.setValidated(true);
			adminApiUser.setRoles(Set.of(adminRole));
			passwordUtils.setApiUserPassword(adminApiUser, DEFAULT_ADMIN_PASSWORD);
			apiUserRepository.save(adminApiUser);
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
