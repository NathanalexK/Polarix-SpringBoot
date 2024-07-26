-- CREATE DATABASE polaire;
-- \c polaire;

CREATE TABLE "group"
(
    "id"      bigserial    NOT NULL,
    "name"    VARCHAR(100) NOT NULL,
    "picture" VARCHAR(255) NOT NULL,
    "founder" BIGINT       NOT NULL
);
ALTER TABLE
    "group"
    ADD PRIMARY KEY ("id");
CREATE TABLE "note"
(
    "id"         BIGSERIAL                      NOT NULL,
    "id_user"    BIGINT                         NOT NULL,
    "id_privacy" BIGINT                         NOT NULL,
    "content"    VARCHAR(100)                   NOT NULL,
    "date"       TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL
);
ALTER TABLE
    "note"
    ADD PRIMARY KEY ("id");
CREATE TABLE "post"
(
    "id"         bigserial    NOT NULL,
    "id_user"    BIGINT       NOT NULL,
    "date"       DATE         NOT NULL,
    "text"       BIGINT       NOT NULL,
    "picture"    VARCHAR(255) NOT NULL,
    "id_privacy" SMALLINT     NOT NULL
);
ALTER TABLE
    "post"
    ADD PRIMARY KEY ("id");
CREATE TABLE "app_user_role"
(
    "id"   SMALLINT    NOT NULL,
    "name" VARCHAR(35) NOT NULL
);
ALTER TABLE
    "app_user_role"
    ADD PRIMARY KEY ("id");
CREATE TABLE "post_like"
(
    "id"      bigserial NOT NULL,
    "id_user" BIGINT    NOT NULL,
    "id_post" BIGINT    NOT NULL,
    "date"    DATE      NOT NULL
);
ALTER TABLE
    "post_like"
    ADD PRIMARY KEY ("id");
CREATE TABLE "friend"
(
    "id"          bigserial NOT NULL,
    "id_sender"   BIGINT    NOT NULL,
    "id_receiver" BIGINT    NOT NULL,
    "date"        DATE      NULL
);
ALTER TABLE
    "friend"
    ADD PRIMARY KEY ("id");
CREATE TABLE "privacy"
(
    "id"   SMALLINT    NOT NULL,
    "name" VARCHAR(35) NOT NULL
);
ALTER TABLE
    "privacy"
    ADD PRIMARY KEY ("id");
CREATE TABLE "group_message"
(
    "id"           bigserial NOT NULL,
    "id_sender"    BIGINT    NOT NULL,
    "id_group"     BIGINT    NOT NULL,
    "date"         DATE      NOT NULL,
    "type_content" SMALLINT  NOT NULL,
    "content"      TEXT      NULL
);
ALTER TABLE
    "group_message"
    ADD PRIMARY KEY ("id");
CREATE TABLE "group_member"
(
    "id"       bigserial NOT NULL,
    "id_group" BIGINT    NOT NULL,
    "id_user"  BIGINT    NOT NULL
);
ALTER TABLE
    "group_member"
    ADD PRIMARY KEY ("id");
CREATE TABLE "post_comment"
(
    "id"        bigserial NOT NULL,
    "id_sender" BIGINT    NOT NULL,
    "id_post"   BIGINT    NOT NULL,
    "date"      DATE      NOT NULL,
    "content"   TEXT      NOT NULL
);
ALTER TABLE
    "post_comment"
    ADD PRIMARY KEY ("id");
CREATE TABLE "message"
(
    "id"            bigserial   NOT NULL,
    "id_sender"     BIGINT      NOT NULL,
    "id_receiver"   BIGINT      NOT NULL,
    "date_time"     TIMESTAMP   NOT NULL,
    "type_content"  SMALLINT    NOT NULL,
    "content"       TEXT        NOT NULL
);
ALTER TABLE
    "message"
    ADD PRIMARY KEY ("id");
CREATE TABLE "app_user"
(
    "id"          bigserial                      NOT NULL,
    "username"    VARCHAR(100)                   NOT NULL,
    "email"       VARCHAR(100)                   NOT NULL,
    "password"    VARCHAR(100)                   NOT NULL,
    "birthdate"   DATE                           NOT NULL,
    "role"        SMALLINT                       NULL,
    "last_online" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "biography"   TEXT                           NULL,
    "created_at"  DATE                           NOT NULL,
    "picture"     VARCHAR(255)                   NULL,
    "sex"         SMALLINT                       NULL
);
CREATE TABLE "sex"
(
    "id"        SMALLINT    NOT NULL,
    "name"      VARCHAR(20)    NOT NULL
);
ALTER TABLE
    "sex"
    ADD PRIMARY KEY ("id");
ALTER TABLE
    "app_user"
    ADD PRIMARY KEY ("id");
ALTER TABLE
    "app_user"
    ADD CONSTRAINT "app_user_username_unique" UNIQUE ("username");
ALTER TABLE
    "note"
    ADD CONSTRAINT "note_id_user_foreign" FOREIGN KEY ("id_user") REFERENCES "app_user" ("id");
ALTER TABLE
    "group_message"
    ADD CONSTRAINT "group_message_id_sender_foreign" FOREIGN KEY ("id_sender") REFERENCES "app_user" ("id");
ALTER TABLE
    "app_user"
    ADD CONSTRAINT "app_user_role_foreign" FOREIGN KEY ("role") REFERENCES "app_user_role" ("id");
ALTER TABLE
    "app_user"
    ADD CONSTRAINT "app_user_sex_foreign" FOREIGN KEY ("sex") REFERENCES "sex" ("id");
ALTER TABLE
    "message"
    ADD CONSTRAINT "message_id_sender_foreign" FOREIGN KEY ("id_sender") REFERENCES "app_user" ("id");
ALTER TABLE
    "group_message"
    ADD CONSTRAINT "group_message_id_group_foreign" FOREIGN KEY ("id_group") REFERENCES "group" ("id");
ALTER TABLE
    "group"
    ADD CONSTRAINT "group_founder_foreign" FOREIGN KEY ("founder") REFERENCES "app_user" ("id");
ALTER TABLE
    "group_member"
    ADD CONSTRAINT "group_member_id_user_foreign" FOREIGN KEY ("id_user") REFERENCES "app_user" ("id");
ALTER TABLE
    "note"
    ADD CONSTRAINT "note_id_privacy_foreign" FOREIGN KEY ("id_privacy") REFERENCES "privacy" ("id");
ALTER TABLE
    "post_like"
    ADD CONSTRAINT "post_like_id_user_foreign" FOREIGN KEY ("id_user") REFERENCES "app_user" ("id");
ALTER TABLE
    "post_comment"
    ADD CONSTRAINT "post_comment_id_sender_foreign" FOREIGN KEY ("id_sender") REFERENCES "app_user" ("id");
ALTER TABLE
    "post"
    ADD CONSTRAINT "post_id_privacy_foreign" FOREIGN KEY ("id_privacy") REFERENCES "privacy" ("id");
ALTER TABLE
    "friend"
    ADD CONSTRAINT "friend_id_sender_foreign" FOREIGN KEY ("id_sender") REFERENCES "app_user" ("id");
ALTER TABLE
    "post_like"
    ADD CONSTRAINT "post_like_id_post_foreign" FOREIGN KEY ("id_post") REFERENCES "post" ("id");
ALTER TABLE
    "group_member"
    ADD CONSTRAINT "group_member_id_group_foreign" FOREIGN KEY ("id_group") REFERENCES "group" ("id");
ALTER TABLE
    "message"
    ADD CONSTRAINT "message_id_receiver_foreign" FOREIGN KEY ("id_receiver") REFERENCES "app_user" ("id");
ALTER TABLE
    "post_comment"
    ADD CONSTRAINT "post_comment_id_post_foreign" FOREIGN KEY ("id_post") REFERENCES "post" ("id");
ALTER TABLE
    "friend"
    ADD CONSTRAINT "friend_id_receiver_foreign" FOREIGN KEY ("id_receiver") REFERENCES "app_user" ("id");
ALTER TABLE
    "post"
    ADD CONSTRAINT "post_id_user_foreign" FOREIGN KEY ("id_user") REFERENCES "app_user" ("id");