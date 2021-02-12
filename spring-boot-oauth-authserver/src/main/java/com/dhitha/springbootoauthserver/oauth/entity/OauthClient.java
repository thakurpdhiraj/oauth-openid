package com.dhitha.springbootoauthserver.oauth.entity;

import com.dhitha.springbootoauthserver.oauth.converter.StringToSetAttributeConverter;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entity for registering oauth clients
 *
 * @author Dhiraj
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OauthClient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "client_id", nullable = false, unique = true, updatable = false)
  private String clientId;

  @Column(name = "client_secret", nullable = false)
  private String clientSecret;

  @Column(name = "client_name", nullable = false)
  @NotNull
  private String clientName;

  @Lob
  @Column(name = "redirect_uri_list", nullable = false)
  @Convert(converter = StringToSetAttributeConverter.class)
  @NotNull
  private Set<String> redirectURIList;

  @Column(name = "authorized_grant_types", nullable = false)
  @Convert(converter = StringToSetAttributeConverter.class)
  private Set<String> authorizedGrantTypes;

  @Column(name = "privacy_policy", nullable = false)
  @NotNull
  private String privacyPolicy;

  @Column(name = "terms_of_service", nullable = false)
  @NotNull
  private String termsOfService;

  @Column(columnDefinition = "TIMESTAMP")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(columnDefinition = "TIMESTAMP")
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
