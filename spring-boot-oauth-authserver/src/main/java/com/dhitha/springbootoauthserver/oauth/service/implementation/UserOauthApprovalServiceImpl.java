package com.dhitha.springbootoauthserver.oauth.service.implementation;

import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.oauth.entity.User;
import com.dhitha.springbootoauthserver.oauth.entity.UserOauthApproval;
import com.dhitha.springbootoauthserver.oauth.repository.UserOauthApprovalRepository;
import com.dhitha.springbootoauthserver.oauth.service.UserOauthApprovalService;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** @author Dhiraj */
@Service
public class UserOauthApprovalServiceImpl implements UserOauthApprovalService {

  private final UserOauthApprovalRepository userOauthApprovalRepository;

  @Autowired
  public UserOauthApprovalServiceImpl(UserOauthApprovalRepository userOauthApprovalRepository) {
    this.userOauthApprovalRepository = userOauthApprovalRepository;
  }

  @Override
  public UserOauthApproval find(User user, OauthClient client) {
    return userOauthApprovalRepository
        .findByUserAndClient(user, client)
        .orElse(new UserOauthApproval());
  }

  @Override
  public UserOauthApproval save(OauthClient client, User user, Set<String> scopes) {
    UserOauthApproval oldApprovals = this.find(user, client);
    System.out.println("old: " + oldApprovals);
    if (oldApprovals == null || oldApprovals.getId() == null) {
      oldApprovals =
          UserOauthApproval.builder().client(client).user(user).approvedScopes(scopes).build();
    } else {
      Set<String> approvedScopes = oldApprovals.getApprovedScopes();
      System.out.println("appScope: " + approvedScopes);
      if (approvedScopes != null) {
        approvedScopes.addAll(scopes);
      }
      System.out.println("new scope: " + approvedScopes);
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
