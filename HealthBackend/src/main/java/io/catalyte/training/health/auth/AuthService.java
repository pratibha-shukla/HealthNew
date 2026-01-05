package io.catalyte.training.health.auth;

public interface AuthService {

  AuthToken login(Credential credentials);
}
