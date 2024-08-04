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