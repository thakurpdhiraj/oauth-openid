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
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entity to store a user's approved scope for a client
 * @author Dhiraj
 */
@Entity
@Table(name = "user_oauth_approval")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOauthApproval implements Serializable {
  private static final long serialVersionUID = 1;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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

  @Column(name = "created_at",columnDefinition = "TIMESTAMP")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column(name = "updated_at",columnDefinition = "TIMESTAMP")
  @UpdateTimestamp
  private LocalDateTime updatedAt;


}
