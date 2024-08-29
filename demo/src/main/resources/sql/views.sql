SELECT DISTINCT username,
                last_online,
                picture
FROM (SELECT id_receiver AS user
      FROM friend
      WHERE friend.id_sender = 14
        AND friend.date_confirm IS NOT NULL
      UNION
      SELECT id_receiver AS user
      FROM friend
      WHERE friend.id_receiver = 14
        AND friend.date_confirm IS NOT NULL) AS a
         JOIN app_user
              ON app_user.id = a.user
;

SELECT post.*,
       app_user.picture,
       CASE
           WHEN id_post IS NULL
               THEN FALSE
           ELSE TRUE
           END AS is_liked
FROM post
         JOIN app_user
              ON post.id_user = app_user.id
         LEFT JOIN (SELECT id_post
                    FROM post_like
                    WHERE id_user = :id_user) AS pl
                   ON post.id = pl.id_post
;

SELECT *
FROM post;

SELECT u1.id   AS id_user,
       CASE
           WHEN f1.id IS NULL AND f2.id IS NULL THEN 'NONE' -- No relationship
           WHEN f1.id IS NOT NULL AND f1.id_receiver = u1.id AND f1.date_confirm IS NULL
               THEN 'SEND' -- Invitation sent by current user, but not confirmed
           WHEN f2.id IS NOT NULL AND f2.id_sender = u1.id AND f2.date_confirm IS NULL
               THEN 'CONFIRM' -- Invitation received by current user, but not answered
           WHEN f1.id IS NOT NULL AND f1.date_confirm IS NOT NULL THEN 'FRIEND' -- They are friends
           END AS type_relation
FROM "app_user" u1
         LEFT JOIN "friend" f1
                   ON (u1.id = f1.id_receiver OR u1.id = f1.id_sender) -- Join on receiver or sender for invitations
         LEFT JOIN "friend" f2 ON (u1.id = f2.id_receiver OR u1.id = f2.id_sender) AND
                                  f2.date_confirm IS NOT NULL -- Join on receiver or sender for friendship
WHERE u1.id <> :id;

SELECT u.id,
       u.username,
       CASE
           WHEN f1.id IS NULL AND f2.id IS NULL THEN 'NONE'
           WHEN f1.id IS NOT NULL AND f1.date_confirm IS NULL THEN 'CONFIRM'
           WHEN f2.id IS NOT NULL AND f2.date_confirm IS NULL THEN 'SEND'
           WHEN (f1.id IS NOT NULL AND f1.date_confirm IS NOT NULL) OR
                (f2.id IS NOT NULL AND f2.date_confirm IS NOT NULL) THEN 'FRIEND'
           END
FROM app_user u
         LEFT JOIN friend f1 ON f1.id_sender = u.id AND f1.id_receiver = :id
         LEFT JOIN friend f2 ON f2.id_receiver = u.id AND f2.id_sender = :id
WHERE u.id <> :id
;