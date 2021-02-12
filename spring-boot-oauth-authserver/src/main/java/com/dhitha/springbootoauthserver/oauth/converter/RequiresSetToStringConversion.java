package com.dhitha.springbootoauthserver.oauth.converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation to enable auto string to set conversion for DTO attributes
 *
 * <p>eg.
 *
 * <pre>
 * {@code @RequiresSetToStringConversion}
 *   private Set<String> attribute;
 * </pre>
 *
 * @author Dhiraj
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresSetToStringConversion {}
