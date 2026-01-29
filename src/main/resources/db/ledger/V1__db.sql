create table sys_user
(
    account_non_expired     boolean      not null,
    account_non_locked      boolean      not null,
    credentials_non_expired boolean      not null,
    enabled                 boolean      not null,
    two_factor_enabled      boolean      not null,
    created_at              bigint       not null,
    created_by              bigint       not null,
    id                      bigint       not null,
    updated_at              bigint       not null,
    updated_by              bigint       not null,
    username                varchar(20)  not null,
    email                   varchar(50)  not null,
    phone                   varchar(50)  not null,
    two_factor_secret       varchar(64),
    password                varchar(120) not null,
    primary key (id),
    constraint uk_user_username unique (username),
    constraint uk_user_email unique (email)
);
comment on table sys_user is '用户表';
comment on column sys_user.account_non_expired is '账户是否未过期（true=有效，false=过期）';
comment on column sys_user.account_non_locked is '账户是否未锁定（true=正常，false=锁定）';
comment on column sys_user.credentials_non_expired is '密码是否未过期（true=有效，false=已过期）';
comment on column sys_user.enabled is '状态（true=启用，false=禁用）';
comment on column sys_user.two_factor_enabled is '是否启用双因素认证（true=启用，false=未启用）';
comment on column sys_user.created_at is '创建时间';
comment on column sys_user.created_by is '创建者';
comment on column sys_user.id is '主键ID';
comment on column sys_user.updated_at is '更新时间';
comment on column sys_user.updated_by is '更新者';
comment on column sys_user.username is '用户名';
comment on column sys_user.email is '邮箱';
comment on column sys_user.phone is '手机号';
comment on column sys_user.two_factor_secret is '双因素认证密钥（TOTP Secret，用于 Google Authenticator 等）';
comment on column sys_user.password is '用户密码';
