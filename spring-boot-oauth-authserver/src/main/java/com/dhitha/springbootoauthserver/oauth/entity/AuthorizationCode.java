package com.dhitha.springbootoauthserver.oauth.entity;

import com.dhitha.springbootoauthserver.oauth.converter.StringToSetAttributeConverter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

/**
 * Entity to store time bound authorization code to be sent back to clients for OAUTH request.
 *
 * @author Dhiraj
 */
@Entity
@Table(name = "authorization_code")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorizationCode implements Serializable {
  private static final long serialVersionUID = 1;

  @Id
  @Column(name = "code", unique = true, nullable = false)
  private String code;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private OauthClient client;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private User user;

  @Column(name = "approved_scopes", nullable = false)
  @Convert(converter = StringToSetAttributeConverter.class)
  private Set<String> approvedScopes;

  @Column(name = "redirect_uri", nullable = false)
  private String redirectUri;

  @Column(name = "nonce", nullable = false)
  private String nonce;

  @Column(name = "is_refresh_required", columnDefinition = "bit(1) default 0")
  private boolean isRefreshRequired;

  @Column(name = "expiration_date", columnDefinition = "TIMESTAMP", nullable = false)
  private LocalDateTime expirationDate;

  @Column(name = "created_at", columnDefinition = "TIMESTAMP")
  @CreationTimestamp
  private LocalDateTime createdAt;
}
