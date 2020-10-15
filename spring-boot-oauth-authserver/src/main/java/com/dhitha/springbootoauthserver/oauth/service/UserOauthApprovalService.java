package com.dhitha.springbootoauthserver.oauth.service;

import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.resource.entity.User;
import com.dhitha.springbootoauthserver.oauth.entity.UserOauthApproval;
import java.util.Set;

/**
 * Fetch scope approvals given by user for a client
 *
 * @author Dhiraj
 */
public interface UserOauthApprovalService {
  UserOauthApproval find(User user, OauthClient client);

  UserOauthApproval save(OauthClient client, User user, Set<String> scopes);

  UserOauthApproval save(UserOauthApproval userOauthApproval);

  boolean delete(UserOauthApproval userOauthApproval);

  boolean delete(OauthClient client, User user);
}
