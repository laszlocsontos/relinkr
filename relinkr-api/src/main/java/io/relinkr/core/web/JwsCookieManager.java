package io.relinkr.core.web;

import static com.nimbusds.jose.JWSAlgorithm.HS256;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import java.text.ParseException;
import java.time.Duration;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

/**
 * Helper class for cookie generation based upon {@link CookieManager}. Main additions to the
 * original implementations are that {@code JwsCookieManager} stores cookies in
 * <a href="https://en.wikipedia.org/wiki/JSON_Web_Signature">JWS</a> format in order
 * to ensure integrity of information and prevent it from being tampering with.
 */
public class JwsCookieManager extends CookieManager {

  private final JWSSigner signer;
  private final JWSVerifier verifier;

  /**
   * Creates a new {@code JwsCookieManager}.
   *
   * @param cookieName Cookie name
   * @param cookieMaxAgeDuration Cookies max age, if null expires immediately
   * @param httpOnly Whether or not this is a
   * <a href="https://www.owasp.org/index.php/HttpOnly">HTTPOnly</a> cookie
   * @param secretKey Secret key used for signing and verifying the signature
   */
  public JwsCookieManager(
      String cookieName, Duration cookieMaxAgeDuration, boolean httpOnly, String secretKey) {

    super(cookieName, cookieMaxAgeDuration, httpOnly);

    try {
      signer = new MACSigner(secretKey);
      verifier = new MACVerifier(secretKey);
    } catch (JOSEException e) {
      // TODO: Add RuntimeException for system errors
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates a new {@code JwsCookieManager}.
   *
   * @param cookieName Cookie name
   * @param cookieMaxAgeDuration Cookies max age, if null expires immediately
   * @param secretKey Secret key used for signing and verifying the signature
   */
  public JwsCookieManager(String cookieName, Duration cookieMaxAgeDuration, String secretKey) {
    this(cookieName, cookieMaxAgeDuration, false, secretKey);
  }

  @Override
  public Optional<String> getCookie(HttpServletRequest request) {
    return super.getCookie(request).map(this::parseAndVerify);
  }

  @Override
  protected Cookie createCookie(String cookieValue) {
    if (!StringUtils.hasText(cookieValue)) {
      return super.createCookie("");
    }

    JWSObject jwsObject = new JWSObject(new JWSHeader(HS256), new Payload(cookieValue));

    try {
      jwsObject.sign(signer);
    } catch (JOSEException e) {
      // TODO: Add RuntimeException for system errors
      throw new RuntimeException(e);
    }

    String signedCookieValue = jwsObject.serialize();

    if (logger.isDebugEnabled()) {
      logger.debug(
          "Signed cookie with name [" + getCookieName() + "] and value [" + cookieValue
              + "] is [" + signedCookieValue + "]");
    }

    return super.createCookie(signedCookieValue);
  }

  private String parseAndVerify(String jwsCookieValue) {
    JWSObject jwsObject;
    try {
      jwsObject = JWSObject.parse(jwsCookieValue);
    } catch (ParseException e) {
      // jwsCookieValue could not be parsed to a valid JWS object
      return null;
    }

    try {
      if (jwsObject.verify(verifier)) {
        return jwsObject.getPayload().toString();
      }
    } catch (IllegalStateException | JOSEException e) {
      // If the JWS object couldn't be verified
    }

    return null;
  }

}
