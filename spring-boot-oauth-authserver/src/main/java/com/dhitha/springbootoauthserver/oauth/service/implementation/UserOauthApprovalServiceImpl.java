package com.dhitha.springbootoauthserver.oauth.service.implementation;

import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.oauth.entity.User;
import com.dhitha.springbootoauthserver.oauth.entity.UserOauthApproval;
import com.dhitha.springbootoauthserver.oauth.repository.UserOauthApprovalRepository;
import com.dhitha.springbootoauthserver.oauth.service.UserOauthApprovalService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation for {@link UserOauthApprovalService}
 *
 * @author Dhiraj
 */
@Service
@RequiredArgsConstructor
public class UserOauthApprovalServiceImpl implements UserOauthApprovalService {

  private final UserOauthApprovalRepository userOauthApprovalRepository;

  @Override
  public UserOauthApproval find(User user, OauthClient client) {
    return userOauthApprovalRepository
        .findByUserAndClient(user, client)
        .orElse(new UserOauthApproval());
  }

  @Override
  public UserOauthApproval save(OauthClient client, User user, Set<String> scopes) {
    UserOauthApproval oldApprovals = this.find(user, client);
    if (oldApprovals == null || oldApprovals.getId() == null) {
      oldApprovals =
          UserOauthApproval.builder().client(client).user(user).approvedScopes(scopes).build();
    } else {
      Set<String> approvedScopes = oldApprovals.getApprovedScopes();
      if (approvedScopes != null) {
        approvedScopes.addAll(scopes);
      }
      oldApprovals.setApprovedScopes(approvedScopes);
    }
    return this.save(oldApprovals);
  }

  @Override
  public UserOauthApproval save(UserOauthApproval userOauthApproval) {
    return userOauthApprovalRepository.saveAndFlush(userOauthApproval);
  }

  @Override
  public boolean delete(UserOauthApproval userOauthApproval) {
    return this.delete(userOauthApproval.getClient(), userOauthApproval.getUser());
  }

  @Override
  public boolean delete(OauthClient client, User user) {
    return userOauthApprovalRepository.deleteByUserAndClient(user, client) == 1;
  }
}
