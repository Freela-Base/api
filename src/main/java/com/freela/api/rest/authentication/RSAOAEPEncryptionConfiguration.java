package com.freela.api.rest.authentication;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import io.micronaut.context.annotation.Value;
import io.micronaut.security.token.jwt.encryption.rsa.RSAEncryptionConfiguration;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Optional;

@Named("generator")
@Singleton
public class RSAOAEPEncryptionConfiguration implements RSAEncryptionConfiguration {
	private RSAPrivateKey rsaPrivateKey;
	private RSAPublicKey rsaPublicKey;
	JWEAlgorithm jweAlgorithm = JWEAlgorithm.RSA_OAEP_256;
	EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;

	public RSAOAEPEncryptionConfiguration(@Value("${pem.path}") String pemPath) {
		Optional<KeyPair> keyPair = KeyPairProvider.keyPair(pemPath);
		if (keyPair.isPresent()) {
			this.rsaPublicKey = (RSAPublicKey) keyPair.get().getPublic();
			this.rsaPrivateKey = (RSAPrivateKey) keyPair.get().getPrivate();
		}
	}

	@Override
	public RSAPublicKey getPublicKey() {
		return rsaPublicKey;
	}

	@Override
	public RSAPrivateKey getPrivateKey() {
		return rsaPrivateKey;
	}

	@Override
	public JWEAlgorithm getJweAlgorithm() {
		return jweAlgorithm;
	}

	@Override
	public EncryptionMethod getEncryptionMethod() {
		return encryptionMethod;
	}
}
