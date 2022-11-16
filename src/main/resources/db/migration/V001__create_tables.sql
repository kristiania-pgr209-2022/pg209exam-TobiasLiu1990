CREATE TABLE users (
    user_id INT IDENTITY PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    favorite_color VARCHAR(100) NOT NULL,
    email_address VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE conversations (
    conversation_id INT IDENTITY PRIMARY KEY,
    conversation_title VARCHAR(200)
);

CREATE TABLE messages (
    message_id INT IDENTITY PRIMARY KEY,
    sender_id INT,
    CONSTRAINT fk_sender_user_id FOREIGN KEY(sender_id) REFERENCES users(user_id),
    created DATETIME2 DEFAULT GETDATE() AT TIME ZONE 'UTC',
    content VARCHAR(4000),
    conversation_id INT,
    CONSTRAINT fk_messages_conversation FOREIGN KEY(conversation_id) REFERENCES conversations(conversation_id)
);


CREATE TABLE conversation_members (
    user_id INT,
    CONSTRAINT fk_conversation_user_id FOREIGN KEY(user_id) REFERENCES users(user_id),
    conversation_id INT,
    CONSTRAINT fk_conversation_member FOREIGN KEY(conversation_id) REFERENCES conversations(conversation_id)
);