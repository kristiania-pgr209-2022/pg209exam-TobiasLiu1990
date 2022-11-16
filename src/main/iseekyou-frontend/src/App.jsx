import './App.css'
import * as React from "react";
import {useEffect, useState} from "react";

//Shows all users
function ListUsers({user, setUser}) {
    const [loading, setLoading] = useState(true);
    const [users, setUsers] = useState([]);

    useEffect(() => {
        (async () => {
            const res = await fetch("/api/user");
            setUsers(await res.json());
            setLoading(false);
        })();
    }, []);

    if (loading) {
        return <div>Loading user...</div>
    }

    //the empty <option></option> works as placeholder. Also, so anything below can be picked.
    return (
        <>
            <div id="show-users-drop-list">
                <h2>User list</h2>
                <h5 id="selected-user">Username: {user && user.fullName}</h5>
                <select value={users} onChange={(e) => setUser(JSON.parse(e.target.value))}>
                    <option id="first-option">Select a user to view conversation and messages</option>

                    {users.map(({id, fullName, email}) => (
                        <option key={id} value={JSON.stringify({id, fullName, email})}>{id} {fullName} {email}</option>
                    ))}
                </select>
            </div>
        </>
    );
}

function SetUserColor(color) {
    document.getElementById("app-title").style.color = color;
}

function UpdateUserSettings({user}) {
    const [fullName, setFullName] = useState("");
    const [email, setEmail] = useState("");
    const [color, setColor] = useState("");

    async function handleSubmit(e) {
        e.preventDefault();

        const res = await fetch("/api/user/settings?userId=" + user.id, {
            method: "put",
            body: JSON.stringify({fullName, email, color}),
            headers: {
                "Content-Type": "application/json",
            },
        });
        if (res.ok) {
            user.color = color;
            user.email = email;
            user.fullName = fullName;
        }
    }

    return (
        <div id="user-settings">
            <form onSubmit={handleSubmit}>
                <div>
                    <label>
                        New name:
                        <input
                            type="test"
                            value={fullName}
                            onChange={(e) => setFullName(e.target.value)}
                        />
                    </label>
                </div>
                <div>
                    <label>
                        New E-mail address:{" "}
                        <input
                            type="text"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                    </label>
                </div>
                <div>
                    <label>
                        New favorite color:{" "}
                        <input
                            type="text"
                            value={color}
                            onChange={(e) => setColor(e.target.value)}
                        />
                    </label>
                </div>
                <button>Apply changes</button>
            </form>
        </div>
    )
}

//Takes messages + setMessages state to pass to ShowMessageBox.
function ShowConversationForUser({user, messages, setMessages}) {
    const [loading, setLoading] = useState(true);
    const [conversation, setConversation] = useState([]);
    const [conversationId, setConversationId] = useState(0);
    const [participants, setParticipants] = useState([]);

    //Show all conversations for current user
    useEffect(() => {
        (async () => {
            const res = await fetch("/api/user/inbox?userId=" + user.id);
            setConversation(await res.json());
            setLoading(false);
        })();
    }, [user.id]);

    //Show all participants in each conversation
    useEffect(() => {
        (async () => {
            const res = await fetch(
                "/api/user/inbox/conversation/members?userId=" + user.id +
                "&cId=" + conversationId);
            setParticipants(await res.json());
        })();
    }, [conversationId])

    if (loading) {
        return <div>Loading conversations...</div>
    }

    //Sets the reply button to visible when a conversation is opened.
    function handleClick(e) {
        setConversationId(e.target.value);
        document.getElementById("reply-div").style.visibility = "visible";
    }

    return (
        <div>
            <h2>Conversations</h2>

            <h5 style={{border: "solid black 1px"}}>
                Members in each conversation <hr></hr>
                {participants.map((p) => (<div>{p}</div>))}
            </h5>

            {conversation.map((c) => (
                <div>
                    <button key={c.id} onClick={handleClick}
                            value={c.id}>{c.id} - {c.conversationTitle}</button>
                </div>
            ))}

            <ShowMessageBox conversationId={conversationId} messages={messages} setMessages={setMessages}/>
        </div>
    );
}

//Should show chat messages in a conversation
function ShowMessageBox({conversationId, messages, setMessages}) {
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        (async () => {
            const res = await fetch("/api/user/inbox/messages?conversationId=" + conversationId);
            setMessages(await res.json());
            setLoading(false);
        })();
    }, [conversationId]);

    if (loading) {
        return <div>Loading messages...</div>
    }

    return (
        <div id="show-messages-div">
            {messages.map((m) => (
                <>
                    <h4>{m.senderName} - {m.messageDate}</h4>
                    <p>{m.content}</p>
                </>
            ))}

            <ReplyToMessage/>
        </div>
    );
}

//Should return the reply posted. Gets added on top of all the prev. messages.
function ReplyToMessage({setMessages}) {
    const [reply, setReply] = useState("");

    async function handleSubmit() {
        const res = await fetch("api/user/inbox/conversation/reply", {
            method: "post",
            body: JSON.stringify({}),
            headers: {
                "Content-Type": "application/json",
            },
        });
        //Append new message to the messages state
        if (res.ok) {
            setMessages((oldMessages) => [...oldMessages, res.json()]);
        }
    }

    return (
        <div id="reply-div" style={{visibility: "hidden"}}>
            <form onSubmit={handleSubmit}>
                <input type="text" value={reply}
                       onChange={(e) => setReply(e.target.value)}></input>
                <button>Reply</button>
            </form>
        </div>
    )
}

//1st param: user - is passed to the CreateMessage component.
//2nd param: recipients - is a list of the people you can send to.
function CreateNewConversation({user, recipients}) {
    const [conversationTitle, setConversationTitle] = useState("");
    const [conversationId, setConversationId] = useState(0);
    let recipientId = 0;

    //POST - create new conversation object with title only. Then returns its ID.
    async function handleSubmitConversationTitle(e) {
        e.preventDefault();

        const res = await fetch("/api/user/inbox/new/conversation", {
            method: "post",
            body: JSON.stringify({conversationTitle}),
            headers: {
                "Content-Type": "application/json",
            },
        });
        //Set conversation title div to hidden after successful post.
        if (res.ok) {
            document.getElementById("new-conversation-title-div").style.visibility = "hidden";
            setConversationId(await res.json());
        }
    }

    //POST - Uses ID from conversation title above + recipientID (who you want to send to)
    async function handleSubmitRecipients(e) {
        e.preventDefault();
        recipientId = e.target.value;

        const res = await fetch("api/user/inbox/new/conversation/addRecipients", {
            method: "post",
            body: JSON.stringify({recipientId, conversationId}),
            headers: {
                "Content-Type": "application/json",
            },
        });
        //Set recipient buttons to hidden after successful post.
        if (res.ok) {
            document.getElementById(recipientId).style.visibility = "hidden";
        }
    }

    return (
        <div id="new-conversation-div">
            <h2>New conversation</h2>
            <hr></hr>

            <div id="new-conversation-title-div">
                <form onSubmit={handleSubmitConversationTitle}>
                    <label>
                        Conversation title:
                        <input type="text"
                               value={conversationTitle}
                               onChange={(e) => setConversationTitle(e.target.value)}
                        />
                    </label>
                    <button>Submit conv</button>
                </form>
            </div>

            <div id="new-recipients-div">
                <h4>Recipients: </h4>
                {recipients.map((r) => (
                    <button id={r.id} value={r.id}
                            onClick={handleSubmitRecipients}>{r.id} - {r.email}</button>
                ))}
            </div>

            <div>
                <CreateMessage conversationId={conversationId} userId={user.id}/>
            </div>
        </div>
    )
}

function CreateMessage({conversationId, userId}) {
    const [content, setContent] = useState("");
    let senderId = userId;


    async function handleSubmit(e) {
        e.preventDefault();
        setContent(e.target.value);
        console.log("Sender id: " + senderId)
        console.log("Content to send: " + content)
        console.log("Conversion Id: " + conversationId)

        const res = await fetch("api/user/inbox/new/conversation/message", {
            method: "post",
            body: JSON.stringify({senderId, content, conversationId}),
            headers: {
                "Content-Type": "application/json",
            },
        });
        //Set submit message div to hidden after successful post. Also hides remaining recipients.
        if (res.ok) {
            document.getElementById("new-message-div").style.visibility = "hidden";
            document.getElementById("new-recipients-div").style.visibility = "hidden";
        }
        //Append new message to the messages state
        // setMessages((oldMessages) => [...oldMessages, res.json()]);
    }

    return (
        <div id="new-message-div">
            <form onSubmit={handleSubmit}>
                <label>
                    Message:
                    <textarea
                        id="message-input"
                        type="text"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}></textarea>
                </label>
                <button>Submit message</button>
            </form>
        </div>
    )
}

function FindRecipientsToAdd({user, recipients, setRecipients}) {
    useEffect(() => {
        const fetchUsers = async () => {
            const res = await fetch("/api/user/inbox/new/conversationRecipients?userId=" + user.id);
            setRecipients(await res.json());
        }
        fetchUsers()
            .catch(console.error);
        console.log(fetchUsers());
    }, []);

    return recipients;
}

function App() {
    const [user, setUser] = useState();
    const [recipients, setRecipients] = useState([]);
    const [messages, setMessages] = useState([]);

    return (
        <div className="App">
            <h1 id="app-title">I Seek You</h1>

            <ListUsers user={user} setUser={setUser}/>
            {user && <SetUserColor user={user}/>}
            {user && <UpdateUserSettings user={user}/>}
            {user && <ShowConversationForUser user={user} messages={messages} setMessages={setMessages}/>}
            {user && <FindRecipientsToAdd user={user} recipient={recipients} setRecipients={setRecipients}/>}
            {user && <CreateNewConversation user={user} recipients={recipients}/>}
        </div>
    );
}

export default App;