package it.palex.attendanceManagement.commons.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.palex.attendanceManagement.commons.exeptions.TokenCannotBeRefreshed;
import it.palex.attendanceManagement.data.entities.auth.UsersAuthDetails;
import it.palex.attendanceManagement.library.utils.DateUtility;

/**
 * @author Alessandro Pagliaro
 *
 * 
 */
@Component
public class JwtTokenUtil implements Serializable {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JwtTokenUtil.class);

	private static final long serialVersionUID = 1L;

	/*
	 * I Claims possibili sono registrati dall'IANA ma si possono modificare o
	 * aggiungerne di altri. Aggiungendo altri Claims custom per� di potrebbero
	 * avere riguardanti le collisioni Nella seguente classe i claims registrati
	 * dall'IANA sono indicati con IANA_XXX quelli custom con CUSTOM_
	 * 
	 * I CLAIM SONO CASE INSENSITIVE
	 */

	// questo parametro indica il Principal che detiene il JWT nel nostro caso la
	// username
	public static final String IANA_CLAIM_KEY_USERNAME = "sub";

	// il parametro audience(aud) indica i destinatari a cui il JWT � destinato
	public static final String IANA_CLAIM_KEY_AUDIENCE = "aud";

	// il parametro custom per indicare la data di creazione del token
	public static final String CUSTOM_CLAIM_KEY_CREATED = "created";

	// il parametro custom per indicare a quale azienda appartiene l'utente
	public static final String CUSTOM_CLAIM_KEY_ID_CLIENTE = "idcliente";
	
	public static final String CUSTOM_CLAIM_KEY_PERMISSION_GROUP = "permission_group";
	
	public static final String CUSTOM_CLAIM_KEY_USERNAME = "username";

	// indica l'expiration time dopo il quale il jwt non deve essere pi� accettato
	public static final String IANA_CLAIM_KEY_EXPIRED = "exp";

	public static final String AUDIENCE_UNKNOWN = "unknown";
	public static final String AUDIENCE_WEB = "web";
	public static final String AUDIENCE_MOBILE = "mobile";
	public static final String AUDIENCE_TABLET = "tablet";

	@Value("${security.jwt.secret}")
	private String secret;

	public Integer getUserIDFromToken(String token) {
		if (token == null) {
			return null;
		}
		final Integer id;
		try {
			final Claims claims = getClaimsFromToken(token);
			if (claims == null) {
				return null;
			}
			try {
				id = (Integer) claims.get(CUSTOM_CLAIM_KEY_ID_CLIENTE);
				return id;
			} catch (ClassCastException e) {
				logger.warn("ID not valid in token received");
				return null;
			}
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			logger.warn("Token expired", e);
			return null;
		} catch (Exception e) {
			logger.error("Error during getIDClienteFromToken", e);
			return null;
		}
	}

	public String getUsernameFromToken(String token) {
		if (token == null) {
			return null;
		}
		String username = null;
		try {
			final Claims claims = getClaimsFromToken(token);
			if (claims == null) {
				return null;
			}
			username = claims.getSubject();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error during getUsernameFromToken", e);
		}
		return username;
	}

	public Date getCreatedDateFromToken(String token) {
		if (token == null) {
			return null;
		}
		Date created = null;
		try {
			final Claims claims = getClaimsFromToken(token);
			if (claims == null) {
				return null;
			}
			created = new Date((Long) claims.get(CUSTOM_CLAIM_KEY_CREATED));
		} catch (Exception e) {
			logger.error("Error during getCreatedDateFromToken", e);
		}
		return created;
	}

	public Date getExpirationDateFromToken(String token) {
		if (token == null) {
			return null;
		}
		Date expiration = null;
		try {
			final Claims claims = getClaimsFromToken(token);
			if (claims == null) {
				return null;
			}
			expiration = claims.getExpiration();
		} catch (Exception e) {
			logger.error("Error during getExpirationDateFromToken", e);
		}
		return expiration;
	}

	/*
	 * public String getAudienceFromToken(String token) { if(token==null){ return
	 * null; } String audience = null; try { final Claims claims =
	 * getClaimsFromToken(token); if(claims==null){ return null; } audience =
	 * (String) claims.get(IANA_CLAIM_KEY_AUDIENCE); } catch (Exception e) {
	 * logger.log(Level.ERROR, Arrays.toString(e.getStackTrace())); } return
	 * audience; }
	 * 
	 * 
	 * private Boolean ignoreTokenExpiration(String token) { String audience =
	 * getAudienceFromToken(token); return (AUDIENCE_TABLET.equals(audience) ||
	 * AUDIENCE_MOBILE.equals(audience)); }
	 *
	 *
	 */

	private Claims getClaimsFromToken(String token) {
		if (token == null) {
			return null;
		}
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		if (token == null) {
			return true;
		}
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(DateUtility.getCurrentDateInUTC());
	}

	public String generateToken(UsersAuthDetails userDetails, Date expirationDate) {
		if (userDetails == null || expirationDate == null) {
			throw new NullPointerException();
		}
		Map<String, Object> claims = new HashMap<>();

		// claims.put(IANA_CLAIM_KEY_USERNAME, userDetails.getUsername());

		String permissionGroupName= "";
		if(userDetails.getPermissionGroup()!=null) {
			permissionGroupName = userDetails.getPermissionGroup().getName();
		}
		
		final Date createdDate = DateUtility.getCurrentDateInUTC();
		claims.put(CUSTOM_CLAIM_KEY_CREATED, createdDate);
		claims.put(CUSTOM_CLAIM_KEY_PERMISSION_GROUP, permissionGroupName);
		claims.put(CUSTOM_CLAIM_KEY_USERNAME, userDetails.getUsername()+"");
		claims.put(CUSTOM_CLAIM_KEY_ID_CLIENTE, userDetails.getId());

		return doGenerateToken(claims, expirationDate);
	}

	private String doGenerateToken(Map<String, Object> claims, Date expirationDate) {
		final Date createdDate = (Date) claims.get(CUSTOM_CLAIM_KEY_CREATED);

		logger.debug("doGenerateToken " + createdDate);

		/*
		 * SignatureAlgorithm.HS512 ---> HS512("HS512", "HMAC using SHA-512", "HMAC",
		 * "HmacSHA512", true) SignatureAlgorithm.RS512 ---> RS512("RS512",
		 * "RSASSA-PKCS-v1_5 using SHA-512", "RSA", "SHA512withRSA", true)
		 */
		return Jwts.builder().setClaims(claims).setIssuedAt(createdDate).setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	/**
	 * 
	 * @param token
	 * @param lastPasswordReset
	 * @param newExpirationDate
	 * @return refreshedToken
	 * @throws TokenCannotBeRefreshed if token cannot be refreshed
	 * @throws NullPointerException if token or newExpirationDate is null
	 */
	public String refreshToken(String token, Date lastPasswordReset, Date newExpirationDate) {
		if (token == null || newExpirationDate == null) {
			throw new NullPointerException();
		}
		if (!canTokenBeRefreshed(token, lastPasswordReset)) {
			throw new TokenCannotBeRefreshed("The token cannot be refreshed");
		}
		String refreshedToken = null;
		try {
			final Date createdDate = DateUtility.getCurrentDateInUTC();
			final Claims claims = getClaimsFromToken(token);
			claims.setIssuedAt(createdDate);
			claims.setExpiration(newExpirationDate);

			refreshedToken = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();

		} catch (Exception e) {
			logger.error("Error during token refresh", e);
		}
		return refreshedToken;
	}

	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = getCreatedDateFromToken(token);
		return !created.before(lastPasswordReset) && (!isTokenExpired(token));
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		if(userDetails==null) {
			return false;
		}
		UsersAuthDetails user = (UsersAuthDetails) userDetails;
		final Integer id = this.getUserIDFromToken(token);

		if (id == null) {
			return false;
		}
		final Date created = getCreatedDateFromToken(token);
		// final Date expiration = getExpirationDateFromToken(token);
		return (id.equals(user.getId()) && !isTokenExpired(token) && !isCreatedBeforeLastPasswordReset(created, user));

	}

	private Boolean isCreatedBeforeLastPasswordReset(Date created, UsersAuthDetails user) {
		Date lastPasswordChange = user.getLastPasswordChangeDate();
		if (lastPasswordChange != null) {
			return created.before(lastPasswordChange);
		}
		// TODO Creare il metodo che controlla che l'ultimo reset della password
		return false;
	}

}
