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

INSERT INTO messages (sender, content, conversation_id)
VALUES ('Ola Nordman', 'First message in the first conversation between Ola and Snorre', 1);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Ola Nordman', 'Sorry for spaming you guys but I really want to see if this app works', 1);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Snorre Snorreson', 'Well hello there snorre how are you doing?', 1);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Elon Musk', 'Hey Ghandi peace is for bitches, am I right Ola?', 2);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Mahatma Ghandi', 'You are an asshole Elon..', 2);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Ola Nordman', 'Yeah Elon, go back to twitter!', 2);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Snorre Snorreson', 'Hello Everyone, can all of you answer in chronological order ordered by user id?', 3);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Ola Nordman', 'Hello this is Ola my id is 1', 3);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Snorre Snorreson', 'And i am the thread starter but my id is 2', 3);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Tobias Tobiasson', 'Me llamo tobias y mi id es 3', 3);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Elon Musk', 'I am a dumb rich entitled child who owns beep beep and tweet tweet companies, and my id is 4', 3);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Mahamta Ghandi', 'I am curry peace indian boy and my id is 5', 3);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Elon Musk', 'Hey guys i would like to start purchasing humans, which one of you is cheapest?', 4);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Tobias Tobiasson', 'I cost 100 thousand kronor, no pruting allowed!', 4);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Snorre Snorreson', 'That is alot of money, I cost a baconpölse and 2 coca colas!', 4);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Elon Musk', 'Alright Snorre, you are now my property and I will turn you into a vaccum-cleaner.', 4);

INSERT INTO messages (sender, content, conversation_id)
VALUES('Elon Musk', '... And I will use you for.. Vaccum cleaning.. of course!', 4);