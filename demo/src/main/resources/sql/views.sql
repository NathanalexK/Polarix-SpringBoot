select distinct
    username,
    last_online,
    picture
from(
select
    id_receiver as user
from friend
where friend.id_sender = 14 and friend.date_confirm is not null
union
select
    id_receiver as user
from friend
where friend.id_receiver = 14 and friend.date_confirm is not null) as a
join app_user
on app_user.id = a.user
;

select
    post.*,
    app_user.picture,
    case when id_post is null
    then false
    else true
    end as is_liked
from post
join app_user
on post.id_user = app_user.id
left join (
    select
        id_post
    from post_like
    where id_user = :id_user
) as pl
on post.id = pl.id_post
;

select * from post;

SELECT
    u1.id AS id_user,
    CASE
        WHEN f1.id IS NULL AND f2.id IS NULL THEN 'NONE'  -- No relationship
        WHEN f1.id IS NOT NULL AND f1.id_receiver = u1.id AND f1.date_confirm IS NULL THEN 'SEND'  -- Invitation sent by current user, but not confirmed
        WHEN f2.id IS NOT NULL AND f2.id_sender = u1.id AND f2.date_confirm IS NULL THEN 'CONFIRM'  -- Invitation received by current user, but not answered
        WHEN f1.id IS NOT NULL AND f1.date_confirm IS NOT NULL THEN 'FRIEND'  -- They are friends
        END AS type_relation
FROM
    "app_user" u1
        LEFT JOIN "friend" f1 ON (u1.id = f1.id_receiver OR u1.id = f1.id_sender) -- Join on receiver or sender for invitations
        LEFT JOIN "friend" f2 ON (u1.id = f2.id_receiver OR u1.id = f2.id_sender) AND f2.date_confirm IS NOT NULL -- Join on receiver or sender for friendship
WHERE u1.id <> :id;