

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

### PasswordEncoder

```java
package org.springframework.security.crypto.password;

public interface PasswordEncoder {

	/**
	 * Encode the raw password. Generally, a good encoding algorithm applies a SHA-1 or
	 * greater hash combined with an 8-byte or greater randomly generated salt.
	 */
	String encode(CharSequence rawPassword);

	/**
	 * Verify the encoded password obtained from storage matches the submitted raw
	 * password after it too is encoded. Returns true if the passwords match, false if
	 * they do not. The stored password itself is never decoded.
	 * @param rawPassword the raw password to encode and match
	 * @param encodedPassword the encoded password from storage to compare with
	 * @return true if the raw password, after encoding, matches the encoded password from
	 * storage
	 */
	boolean matches(CharSequence rawPassword, String encodedPassword);

	/**
	 * Returns true if the encoded password should be encoded again for better security,
	 * else false. The default implementation always returns false.
	 * @param encodedPassword the encoded password to check
	 * @return true if the encoded password should be encoded again for better security,
	 * else false.
	 */
	default boolean upgradeEncoding(String encodedPassword) {
		return false;
	}

}
```

### UserDetailsManager

```java
package org.springframework.security.provisioning;

public interface UserDetailsManager extends UserDetailsService {

	/**
	 * Create a new user with the supplied details.
	 */
	void createUser(UserDetails user);

	/**
	 * Update the specified user.
	 */
	void updateUser(UserDetails user);

	/**
	 * Remove the user with the given login name from the system.
	 */
	void deleteUser(String username);

	/**
	 * Modify the current user's password. This should change the user's password in the
	 * persistent user repository (database, LDAP etc).
	 * @param oldPassword current password (for re-authentication if required)
	 * @param newPassword the password to change to
	 */
	void changePassword(String oldPassword, String newPassword);

	/**
	 * Check if a user with the supplied login name exists in the system.
	 */
	boolean userExists(String username);

}
```



## 集成Spring Security后的默认状态

### 访问所有端点都需要认证

### 默认认证方式

集成 Spring Security 时，如果未调整默认配置，**HTTP Basic** 则是默认的访问认证方式。

> 使用 HTTP Authentication请求头发送认证信息（用户名和密码）

基本格式

`Authentication: Basic <base64(user:password)>`

HTTP/1.1 使用的认证方式

- BASIC 认证（基本认证）
- DIGEST 认证（摘要认证）
- SSL 客户端认证
- FormBase 认证（基于表单认证）

## 修改默认配置

默认暴露端点 `/hello`

### 用户管理

#### 使用存储到内存的用户信息

添加配置类

```java
@Configuration
public class UserManagementConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        var inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

        var userDetails1 = User.withUsername("user1")
                .password("{noop}password")
                .authorities("read")
                .build();
        var userDetails2 = User.withUsername("user2")
                // "password" encoded with bcrypt
                .password("{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
                .authorities("read")
                .build();

        inMemoryUserDetailsManager.createUser(userDetails1);
        inMemoryUserDetailsManager.createUser(userDetails2);

        return inMemoryUserDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // alternatively, you can customize DelegatingPasswordEncoder by yourself to support target encodings
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
```

验证

```sh
curl http://localhost:8080/hello -u user1:password
curl http://localhost:8080/hello -u user2:password
```

#### 使用存储在数据库中的用户信息

ORM: Spring Data JPA

DB: MySQL 8

##### 聚合应用中的User实体类，并实现`org.springframework.security.core.userdetails.UserDetails`

```java
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
```

##### 实现`org.springframework.security.core.userdetails.UserDetailsService`，使用DAO完成用户查询逻辑

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new CustomUserDetails(user);
    }
}
```

##### 配置`org.springframework.security.crypto.password.PasswordEncoder`

```java
@Configuration
public class UserManagementConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // alternatively, you can customize DelegatingPasswordEncoder by yourself to support target encodings
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
```

验证


```sh
curl http://localhost:8080/hello --user 'Tom:123456'
curl http://localhost:8080/hello --user 'James:password'
```
