# Spring Security From 0 To 1

## 核心组件

### UserDetails

```java
package org.springframework.security.core.userdetails;

public interface UserDetails extends Serializable {

    /**
     * Returns the authorities granted to the user. Cannot return <code>null</code>.
     * @return the authorities, sorted by natural key (never <code>null</code>)
     */
    Collection<? extends GrantedAuthority> getAuthorities();

    /**
     * Returns the password used to authenticate the user.
     * @return the password
     */
    String getPassword();

    /**
     * Returns the username used to authenticate the user. Cannot return
     * <code>null</code>.
     * @return the username (never <code>null</code>)
     */
    String getUsername();

    /**
     * Indicates whether the user's account has expired. An expired account cannot be
     * authenticated.
     * @return <code>true</code> if the user's account is valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    default boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be
     * authenticated.
     * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
     */
    default boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired. Expired
     * credentials prevent authentication.
     * @return <code>true</code> if the user's credentials are valid (ie non-expired),
     * <code>false</code> if no longer valid (ie expired)
     */
    default boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be
     * authenticated.
     * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
     */
    default boolean isEnabled() {
        return true;
    }

}
```

### GrantedAuthority

```java
package org.springframework.security.core;

public interface GrantedAuthority extends Serializable {

	/**
	 * If the <code>GrantedAuthority</code> can be represented as a <code>String</code>
	 * and that <code>String</code> is sufficient in precision to be relied upon for an
	 * access control decision by an {@link AuthorizationManager} (or delegate), this
	 * method should return such a <code>String</code>.
	 * <p>
	 * If the <code>GrantedAuthority</code> cannot be expressed with sufficient precision
	 * as a <code>String</code>, <code>null</code> should be returned. Returning
	 * <code>null</code> will require an <code>AccessDecisionManager</code> (or delegate)
	 * to specifically support the <code>GrantedAuthority</code> implementation, so
	 * returning <code>null</code> should be avoided unless actually required.
	 * @return a representation of the granted authority (or <code>null</code> if the
	 * granted authority cannot be expressed as a <code>String</code> with sufficient
	 * precision).
	 */
	String getAuthority();

}
```

### UserDetailsService

```java
package org.springframework.security.core.userdetails;

public interface UserDetailsService {

	/**
	 * Locates the user based on the username. In the actual implementation, the search
	 * may possibly be case sensitive, or case insensitive depending on how the
	 * implementation instance is configured. In this case, the <code>UserDetails</code>
	 * object that comes back may have a username that is of a different case than what
	 * was actually requested..
	 * @param username the username identifying the user whose data is required.
	 * @return a fully populated user record (never <code>null</code>)
	 * @throws UsernameNotFoundException if the user could not be found or the user has no
	 * GrantedAuthority
	 */
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}
```
