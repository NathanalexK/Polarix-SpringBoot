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
    id_post,
    case when id_post is null
    then false
    else true
    end as is_liked
from post
left join (
    select
        id_post
    from post_like
) as pl
on post.id = pl.id_post

;