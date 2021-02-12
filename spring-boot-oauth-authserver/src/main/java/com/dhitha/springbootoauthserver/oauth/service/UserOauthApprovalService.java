package com.dhitha.springbootoauthserver.oauth.service;

import com.dhitha.springbootoauthserver.oauth.entity.OauthClient;
import com.dhitha.springbootoauthserver.oauth.entity.User;
import com.dhitha.springbootoauthserver.oauth.entity.UserOauthApproval;
import java.util.Set;

/**
 * Fetch scope approvals given by user for a client
 *
 * @author Dhiraj
 */
public interface UserOauthApprovalService {

  /**
   * Find user's oauth approvals for a client
   *
   * @param user -
   * @param client -
   * @return user's oauth approvals
   */
  UserOauthApproval find(User user, OauthClient client);

  /**
   * Save a user's oauth scope approvals for a client
   *
   * @param client -
   * @param user -
   * @param scopes -
   * @return user's saved approval
   */
  UserOauthApproval save(OauthClient client, User user, Set<String> scopes);

  /**
   * Save a user's oauth scope approvals for a client
   *
   * @param userOauthApproval - object containing required user and client information
   * @return user's saved approval
   */
  UserOauthApproval save(UserOauthApproval userOauthApproval);

  /**
   * Delete user's approval for a client
   *
   * @param userOauthApproval - object containing required user and client information
   * @return true if deletion was successful
   */
  boolean delete(UserOauthApproval userOauthApproval);

  /**
   * Delete user's approval for a client
   *
   * @param client -
   * @param user -
   * @return true if deletion was successful
   */
  boolean delete(OauthClient client, User user);
}
