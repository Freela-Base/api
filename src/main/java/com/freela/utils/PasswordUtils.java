package com.freela.utils;

import com.freela.database.model.ApiUser;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import jakarta.inject.Singleton;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Singleton
public class PasswordUtils {
	private static final String HASH_ALGORITHM = "PBKDF2WithHmacSHA512";
	private static final Integer ENCRYPTION_ALGORITHM_KEY_SIZE = 512;
	private static final Integer ENCRYPTION_ALGORITHM_ITERATIONS = 65536;
	private static final Integer DEFAULT_RANDOM_STRING_LENGTH = 32;
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	private static final Base64.Encoder B_64_ENCODER = Base64.getUrlEncoder().withoutPadding();
	private static final Base64.Decoder B_64_DECODER = Base64.getUrlDecoder();

	private final SecretKeyFactory factory;

	@Value("${com.freela.service.api-user.password.salt-size:32}")
	private Integer PASSWORD_SALT_SIZE;

	@Value("${com.freela.service.api-user.password.pepper-size:32}")
	private Integer PASSWORD_PEPPER_SIZE;

	public PasswordUtils() throws NoSuchAlgorithmException {
		factory = SecretKeyFactory.getInstance(HASH_ALGORITHM);
	}

	public boolean isValidPassword(ApiUser apiUser, String password) throws InvalidKeySpecException {
		return apiUser != null
				&& password != null
				&& apiUser.getPasswordHash() != null
				&& apiUser.getPasswordHash().equals(
						hash(password, apiUser.getPasswordSalt(), apiUser.getPasswordPepper()));
	}

	public String hash(String password, String salt, String pepper) throws InvalidKeySpecException {
		String stringBuilder = password + pepper;
		KeySpec spec = new PBEKeySpec(
				stringBuilder.toCharArray(),
				B_64_DECODER.decode(salt),
				ENCRYPTION_ALGORITHM_ITERATIONS,
				ENCRYPTION_ALGORITHM_KEY_SIZE);
		byte[] hash = factory.generateSecret(spec).getEncoded();
		return B_64_ENCODER.encodeToString(hash);
	}

	public String getRandomString(Integer size) {
		if (size == null || size < 0)
			size = DEFAULT_RANDOM_STRING_LENGTH;

		byte[] salt = new byte[size];
		SECURE_RANDOM.nextBytes(salt);
		return B_64_ENCODER.encodeToString(salt);
	}

	public void setApiUserPassword(
			@NonNull ApiUser apiUser,
			@NonNull String password
	) throws InvalidKeySpecException {
		if (StringUtils.isNotEmpty(password)) {
			apiUser.setPasswordSalt(getRandomString(PASSWORD_SALT_SIZE));
			apiUser.setPasswordPepper(getRandomString(PASSWORD_PEPPER_SIZE));
			apiUser.setPasswordHash(hash(
					password,
					apiUser.getPasswordSalt(),
					apiUser.getPasswordPepper()));
		}
	}
}
