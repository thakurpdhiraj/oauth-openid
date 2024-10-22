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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entity to store time bound access token / long lived refresh token
 *
 * @author Dhiraj
 */
@Entity
@Table(name = "access_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "token", unique = true, nullable = false)
  private String token;

  @Column(name = "access_token_expiry", columnDefinition = "TIMESTAMP", nullable = false)
  private LocalDateTime accessTokenExpiry;

  @ManyToOne
  @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private OauthClient client;

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private User user;

  @Column(name = "refresh_scopes")
  private String refreshToken;

  @Column(name = "approved_scopes", nullable = false)
  @Convert(converter = StringToSetAttributeConverter.class)
  private Set<String> approvedScopes;

  @Column(name = "created_at", columnDefinition = "TIMESTAMP")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
