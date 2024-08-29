-- Table: app_role
INSERT INTO public.app_user_role (id, name) VALUES (0, 'admin');
INSERT INTO public.app_user_role (id, name) VALUES (1, 'client');

-- Table: privacy
INSERT INTO public.privacy (id, name) VALUES (0, 'public');
INSERT INTO public.privacy (id, name) VALUES (1, 'private');
INSERT INTO public.privacy (id, name) VALUES (2, 'personal');

-- Table: Conversation_type
INSERT INTO public.conversation_type(id, name) VALUES (0, 'ONE_TO_ONE');
INSERT INTO public.conversation_type(id, name) VALUES (1, 'ONE_TO_MANY');


