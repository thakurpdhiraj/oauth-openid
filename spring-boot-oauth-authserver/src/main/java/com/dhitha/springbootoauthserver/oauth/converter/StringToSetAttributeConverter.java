package com.dhitha.springbootoauthserver.oauth.converter;

import static com.dhitha.springbootoauthserver.oauth.constant.Constants.SCOPE_TOKEN;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Dhiraj
 */
@Converter
public class StringToSetAttributeConverter implements AttributeConverter<Set<String>, String> {

  @Override
  public String convertToDatabaseColumn(Set<String> attribute) {
    return String.join(SCOPE_TOKEN, attribute);
  }

  @Override
  public Set<String> convertToEntityAttribute(String dbData) {
    return new HashSet<>(Arrays.asList(dbData.split(SCOPE_TOKEN)));
  }
}