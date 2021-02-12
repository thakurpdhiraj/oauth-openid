insert into user (id,name,email,account_non_expired,account_non_locked,created_at,credentials_non_expired,enabled,password,updated_at,username)
 values (null,'admin-lms','admin@lms.com',1,1,current_timestamp(),1,1,'$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a',current_timestamp(),'admin');

insert into role (id,name) values (null, 'ROLE_ADMIN');
insert into role (id,name) values (null, 'ROLE_USER');

insert into user_role (user_id,role_id) values (1, 1);
insert into user_role (user_id,role_id) values (1, 2);

insert into oauth_client (id,client_id,client_secret,client_name,redirect_uri_list,authorized_grant_types,created_at,updated_at,privacy_policy,terms_of_service)
 values (null,'app.lms.1','y8X2Yio05kHiTsisYE68','wholesale-books','http://localhost:8181/wholesale','authorization_code',current_timestamp(),current_timestamp(),'http://localhost:8181/privacy','http://localhost:8181/termsofservice');

--insert into boot_oauth_server.user_oauth_approval (id,approved_scopes,created_at,updated_at,client_id,user_id)
--values (null,'openid',current_timestamp(),current_timestamp(),1,1);