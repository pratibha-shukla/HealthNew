package io.catalyte.training.health.auth;

import static io.catalyte.training.health.constants.StringConstants.INVALID_EMAIL_PASSWORD;
import static io.catalyte.training.health.constants.StringConstants.ISSUER;
import static io.catalyte.training.health.constants.StringConstants.ROLES_ATTRIBUTE;
import static io.catalyte.training.health.constants.StringConstants.SECRET_KEY;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import  io.catalyte.training.health.domains.user.User;
import io.catalyte.training.health.domains.user.UserService;
import io.catalyte.training.health.exceptions.BadRequest;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public AuthServiceImpl(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userService = userService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public AuthToken login(Credential credentials) {
    if (credentials.getEmail() == null || credentials.getPassword() == null) {
      throw new BadRequest(INVALID_EMAIL_PASSWORD);
    }

    String email = credentials.getEmail();
    String password = credentials.getPassword();

    User user = userService.getUserByEmail(email);

    if (user == null) {
      throw new BadRequest(INVALID_EMAIL_PASSWORD);
    }

    String passwordOnFile = user.getPassword();
    String userRole = user.getRole();

    if (!bCryptPasswordEncoder.matches(password, passwordOnFile)) {
      throw new BadRequest(INVALID_EMAIL_PASSWORD);
    }


    Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
    String jwtToken =
        JWT.create()
            .withIssuer(ISSUER)
            .withClaim(ROLES_ATTRIBUTE, userRole)
            .withSubject(email)
            .withIssuedAt(new Date())
            .sign(algorithm);

    return new AuthToken(jwtToken);
  }
}
