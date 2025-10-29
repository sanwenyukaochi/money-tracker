INSERT INTO public.sys_user (id, account_non_expired, account_non_locked, credentials_non_expired, email, enabled,
                             password, sign_up_method, two_factor_enabled, two_factor_secret, username)
VALUES (1983401012744032256, true, true, true, 'user@example.com', true,
        '$2a$10$4UooretWqjXHUghjjW1xhOAvwImlLlAjEbAsa.eyJakeXmgJGEhEW', 'email', false, null, 'user'),
       (1983401013075382272, true, true, true, 'admin@example.com', true,
        '$2a$10$DCpm6m1NY8VDnUp4A0RUfO2xMxENsngTaRVmGGHcwvG8ZK76sSXba', 'email', false, null, 'admin');

INSERT INTO public.sys_role (id, code, name)
VALUES (1983401012546899968, 'ROLE_USER', '用户'),
       (1983401012714672128, 'ROLE_ADMIN', '管理员');

INSERT INTO public.sys_user_role_rel (id, role_id, user_id)
VALUES (1983401013033439232, 1983401012546899968, 1983401012744032256),
       (1983401013335429120, 1983401012714672128, 1983401013075382272);

INSERT INTO public.sys_permission (id, code, name, parent_id, request_method, request_url)
VALUES (1983401012744032256, 'user:get', '用户查询', 0, 'GET', '/api/user/info'),
       (1983401012744032257, 'user:post', '用户创建', 1983401012744032256, 'POST', '/api/user/info'),
       (1983401012744032258, 'user:put', '用户修改', 1983401012744032256, 'PUT', '/api/user/info'),
       (1983401012744032259, 'user:delete', '用户删除', 1983401012744032256, 'DELETE', '/api/user/info');

INSERT INTO public.sys_role_permission_rel (id, role_id, permission_id)
VALUES (1983401013033439232, 1983401012714672128, 1983401012744032256),
       (1983401013335429120, 1983401012714672128, 1983401012744032257),
       (1983401013335429121, 1983401012714672128, 1983401012744032258),
       (1983401013335429122, 1983401012714672128, 1983401012744032259),
       (1983401013335429123, 1983401012546899968, 1983401012744032256);