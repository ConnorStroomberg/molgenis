package org.molgenis.security.core;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/** Update an {@link Authentication} with new authorities. */
public interface AuthenticationAuthoritiesUpdater {
  /**
   * Update an {@link Authentication} with new authorities.
   *
   * @return updated authentication or new authentication with updated authorities
   */
  Authentication updateAuthentication(
      Authentication authentication, List<GrantedAuthority> authorities);
}
