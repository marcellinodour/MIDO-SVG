package com.github.cocolollipop.mido_svg.BddQuerries;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class QueriesHelper {

	// Récupère l'authentificator et indique que ce sera celui la à utiliser en cas de demande d'un authentificator par une page
	public static void setDefaultAuthenticator() {
		final Authenticator myAuth = getTokenAuthenticator();
		Authenticator.setDefault(myAuth);
	}

	public static Authenticator getTokenAuthenticator() {
		final String tokenValue;
		try {
			tokenValue = getTokenValue();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		final PasswordAuthentication passwordAuthentication = new PasswordAuthentication("guest_miage",
				tokenValue.toCharArray());
		// camille : je pense que c'est username et mdp=tokenValue
		// mais pq ne pas directement mettre le mdp qu'on a ?
		final Authenticator myAuth = getConstantAuthenticator(passwordAuthentication);
		return myAuth;
	}

	private static String getTokenValue() throws IOException, IllegalStateException {
		final Optional<String> tokenOpt = getTokenOpt();
		return tokenOpt
				.orElseThrow(() -> new IllegalStateException("No token found in environment, in property or in file."));
	}

	private static Optional<String> getTokenOpt() throws IOException {
		{
			final String token = System.getenv("API_password");
			if (token != null) {
				return Optional.of(token);
			}
		}
		{
			final String token = System.getProperty("API_password");
			if (token != null) {
				return Optional.of(token);
			}
		}
		final Path path = Paths.get("API_password.txt");
		if (!Files.exists(path)) {
			return Optional.empty();
		}
		final String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		// content est le contenu du fichier API_password.txt
		return Optional.of(content.replaceAll("\n", ""));
	}
	
	private static Authenticator getConstantAuthenticator(PasswordAuthentication passwordAuthentication) {
		final Authenticator myAuth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return passwordAuthentication;
			}
		};
		return myAuth;
	}

}
