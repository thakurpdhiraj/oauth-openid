package com.dhitha.springbootoauthserver.oauth.converter;

import static com.dhitha.springbootoauthserver.oauth.constant.Constants.SCOPE_TOKEN;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Convert List to String for database persistence
 *
 * @author Dhiraj
 */
@Component
public class StringToSetParamConverter implements Converter<String,Set<String>>, ConditionalConverter {

  @Override
  public Set<String> convert(String source) {
    if(source.isBlank()){
      return null;
    }
    return new HashSet<>(Arrays.asList(source.split(SCOPE_TOKEN)));
  }

  @Override
  public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
    return targetType.hasAnnotation(RequiresSetToStringConversion.class);
  }

}
