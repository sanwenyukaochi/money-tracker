package org.secure.security.authentication.handler.login.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubUserProfile(

        @Schema(description = "GitHub 用户唯一 ID")
        Long id,

        @Schema(description = "用户名/login")
        String login,

        //@Schema(description = "GitHub 内部节点 ID")
        //String nodeId,

        @Schema(description = "头像 URL")
        String avatarUrl,

        //@Schema(description = "Gravatar ID")
        //String gravatarId,

        //@Schema(description = "GitHub API 用户 URL")
        //String url,

        //@Schema(description = "GitHub Web 用户主页 URL")
        //String htmlUrl,

        //@Schema(description = "获取该用户粉丝列表的 API URL")
        //String followersUrl,

        //@Schema(description = "获取该用户关注的用户列表 API URL")
        //String followingUrl,

        //@Schema(description = "用户 Gists API URL")
        //String gistsUrl,

        //@Schema(description = "用户收藏的仓库 API URL")
        //String starredUrl,

        //@Schema(description = "用户订阅的仓库列表 API URL")
        //String subscriptionsUrl,

        //@Schema(description = "用户所属组织 API URL")
        //String organizationsUrl,

        //@Schema(description = "用户仓库列表 API URL")
        //String reposUrl,

        //@Schema(description = "用户事件 API URL")
        //String eventsUrl,

        //@Schema(description = "用户收到的事件 API URL")
        //String receivedEventsUrl,

        //@Schema(description = "用户类型，例如 User 或 Organization")
        //String type,

        //@Schema(description = "用户视图类型，通常私有/公开视图")
        //String userViewType,

        //@Schema(description = "是否是 GitHub 管理员")
        //Boolean siteAdmin,

        @Schema(description = "用户显示名")
        String name,

        //@Schema(description = "公司/组织")
        //String company,

        //@Schema(description = "博客或个人网站")
        //String blog,

        @Schema(description = "所在地区")
        String location,

        @Schema(description = "公开邮箱地址")
        String email,

        //@Schema(description = "是否可被雇佣")
        //Boolean hireable,

        @Schema(description = "用户简介")
        String bio,

        @Schema(description = "Twitter 用户名")
        String twitterUsername

        //@Schema(description = "通知用邮箱")
        //String notificationEmail,

        //@Schema(description = "公开仓库数量")
        //Integer publicRepos,

        //@Schema(description = "公开 Gists 数量")
        //Integer publicGists,

        //@Schema(description = "粉丝数量")
        //Integer followers,

        //@Schema(description = "关注数量")
        //Integer following,

        //@Schema(description = "账户创建时间")
        //String createdAt,

        //@Schema(description = "信息更新时间")
        //String updatedAt,

        //@Schema(description = "私有 Gists 数量")
        //Integer privateGists,

        //@Schema(description = "私有仓库总数")
        //Integer totalPrivateRepos,

        //@Schema(description = "拥有的私有仓库数量")
        //Integer ownedPrivateRepos,

        //@Schema(description = "仓库占用空间（字节）")
        //Integer diskUsage,

        //@Schema(description = "协作成员数量")
        //Integer collaborators,

        //@Schema(description = "是否开启双因素认证")
        //Boolean twoFactorAuthentication,

        //@Schema(description = "账户计划信息")
        //Plan plan
) {
    /*
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Plan(
            @Schema(description = "计划名称，例如 free、Medium")
            String name,

            @Schema(description = "分配空间")
            Integer space,

            @Schema(description = "允许创建的私有仓库数量")
            Integer privateRepos,

            @Schema(description = "协作成员上限")
            Integer collaborators
    ) {
    }
    */
}
