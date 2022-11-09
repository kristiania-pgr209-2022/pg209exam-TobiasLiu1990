CREATE TABLE users (
    user_id INT IDENTITY PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email_address VARCHAR(100) NOT NULL
)

CREATE TABLE conversations (
    conversation_id INT IDENTITY PRIMARY KEY,
    conversation_title VARCHAR(200)
)

CREATE TABLE messages (
    message_id INT IDENTITY PRIMARY KEY,
    sender VARCHAR(100) NOT NULL,
    date SMALLDATETIME default GETUTCDATE(),
    content VARCHAR(4000),
    conversation_id INT FOREIGN KEY REFERENCES conversations(conversation_id)
)

CREATE TABLE conversation_members (
    user_id INT FOREIGN KEY REFERENCES users(user_id) ,
    conversation_id INT FOREIGN KEY REFERENCES conversations(conversation_id)
)