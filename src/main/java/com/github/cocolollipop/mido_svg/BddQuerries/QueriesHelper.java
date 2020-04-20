package com.github.cocolollipop.mido_svg.BddQuerries;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


/**
 * This class build an authenticator for network connection
 * @author plaquette-MIDO
 * @see <a href="https://github.com/Dauphine-MIDO/plaquette-MIDO"> plaquette-mido link </a>
 */
public class QueriesHelper {

	/** 
	 * the method get the authenticator and indicate that is this one which is use when it's required
	 * */
	public static void setDefaultAuthenticator() {
		final Authenticator myAuth = getTokenAuthenticator();
		Authenticator.setDefault(myAuth);
	}

	
	/** 
	 * the method get the token value and build authenticator with this token and the ID
	 * @return myAuth a Authenticator object which contain ID and the token value
	 * @see <a href="https://github.com/Dauphine-MIDO/plaquette-MIDO"> plaquette-mido link </a>
	 * */
	public static Authenticator getTokenAuthenticator() {
		final String tokenValue;
		try {
			tokenValue = getTokenValue();
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		final PasswordAuthentication passwordAuthentication = new PasswordAuthentication("guest_miage",
				tokenValue.toCharArray());

		final Authenticator myAuth = getConstantAuthenticator(passwordAuthentication);
		return myAuth;
	}
	
	/** 
	 * The method get the token option and return the token value
	 * @return tokenOpt a Optional<String> object which contain the token value
	 * */
	private static String getTokenValue() throws IOException, IllegalStateException {
		final Optional<String> tokenOpt = getTokenOpt();
		return tokenOpt
				.orElseThrow(() -> new IllegalStateException("No token found in environment, in property or in file."));
	}

	
	/** 
	 * This method get the API_password by using three possible ways (environment variable,system property, text file) and return the token value
	 * @return An optional String variable. "token" If the API_password is in the environment variable or system property. "content" if the API_pasword is in text file
	 * */
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
		// content in API_password.txt
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
