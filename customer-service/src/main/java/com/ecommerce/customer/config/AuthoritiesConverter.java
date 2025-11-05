package com.ecommerce.customer.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface AuthoritiesConverter extends Converter<String, Object>, Collection<GrantedAuthority> {
}