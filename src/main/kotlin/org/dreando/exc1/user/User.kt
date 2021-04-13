package org.dreando.exc1.user

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

const val DEFAULT_USER_ROLE = "USER"

class User(private val username: String, private val password: String) : UserDetails {
    override fun getAuthorities() = listOf(SimpleGrantedAuthority(DEFAULT_USER_ROLE))
    // For production, some password encoder would be advised
    override fun getPassword() = "{noop}$password"
    override fun getUsername() = username
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}