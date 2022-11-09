INSERT INTO users (full_name, email_address)
VALUES ('Ola Nordman', 'olno@gmail.com'),
       ('Snorre Snorreson', 'snorre@gmail.com'),
       ('Tobias Tobiasson', 'tobe@gmail.com'),
       ('Elon Musk', 'musken@tesla.ru'),
       ('Mahamta Ghandi', 'peace@nowar.in');

INSERT INTO conversations (conversation_title)
VALUES ('A little conversation from Ola to Snorre'),
       ('Another coversation between Elon, Ghandi from Ola'),
       ('A conversation between all the users in the database'),
       ('Elon wants to buy Snorre and Tobias');

INSERT INTO conversation_members(user_id, conversation_id)
VALUES(1, 1), /* convo between ola and snorre */
      (2, 1),
      (4, 2), /* convo between elon ola and ghandi */
      (5,2),
      (1, 2),
      (1, 3), /* convo between all users */
      (2, 3),
      (3, 3),
      (4, 3),
      (5, 3),
      (2, 4), /* convo between elon snorre and tobias */
      (3, 4),
      (4, 4);