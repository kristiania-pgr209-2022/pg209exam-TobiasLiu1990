import './App.css'
import React, {useEffect, useState} from "react";

/*
    -list users
        -show conversation titles
            - show messages in conversation
            - write new message
    -user settings
    -new conversation for user
 */


//Shows all users
function ListUsers() {
    const [loading, setLoading] = useState(true);
    const [users, setUsers] = useState([]);
    const [userId, setUserId] = useState(0);    //Used to pass user id to ShowConversationForUser()

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

    function handleChange(e) {
        setUserId(parseInt(e.target.value));
    }

    //the empty <option></option> works as placeholder. Also, so anything below can be picked.
    return (
        <>
            <div id="show-users-drop-list">
                <h2>User list</h2>
                <h5 id="selected-user"> Username </h5>

                <select value={users} onChange={handleChange}>
                    <option id="first-option">Select a user to view conversation and messages</option>

                    {users.map((u) => (
                        <option key={u.id} value={u.id}>{u.id} {u.fullName} {u.email}</option>
                    ))}
                </select>
            </div>

            <div><SetUsersFavoriteColor id={userId}/></div>

            <div id="user-settings">
                <UserSettings id={userId}/>
            </div>

            <div id="conversations">
                <ShowConversationForUser id={userId}/>
            </div>
        </>
    );
}

function SetUsersFavoriteColor(userId) {
    const [loading, setLoading] = useState(true);
    const [user, setUser] = useState([]);

    useEffect(() => {
        (async () => {
            if (userId.id === 0) {
                return;
            }
            const res = await fetch("/api/user/color?userColor=" + userId.id);
            setUser(await res.json());
            setLoading(false);
        })();
    }, [userId]);

    if (loading) {
        return <div>Logo-color should change soon.......</div>
    } else {
        document.getElementById("selected-user").innerHTML = user.map(u => u.fullName);
        document.getElementById("app-title").style.color = user.map(u => u.color);
    }
}

function UserSettings() {


    return null;
}

//Get all conversations for user.
function ShowConversationForUser(userId) {
    const [loading, setLoading] = useState(true);
    const [conversation, setConversation] = useState([]);
    const [conversationId, setConversationId] = useState(0);
    console.log("Conversation id for user: " + userId.id);  //Remove after just for testing

    useEffect(() => {
        (async () => {
            const res = await fetch("/api/user/inbox?userId=" + userId.id);
            setConversation(await res.json());
            setLoading(false);
        })();
    }, [userId]);

    if (loading) {
        return <div>Loading conversations...</div>
    }

    function handleClick(e) {
        setConversationId(parseInt(e.target.value));
    }

    return (
        <div>
            <h2>Conversations</h2>

            {conversation.map((c) => (
                <div>
                    <button key={c.id} onClick={handleClick} value={c.id}>{c.id} - {c.conversationTitle}</button>
                </div>
            ))}
            <div id="show-messages">
                <ShowMessageBox id={conversationId}/>
            </div>
        </div>
    );
}

//Should show chat messages in a conversation
function ShowMessageBox(conversationId) {
    const [loading, setLoading] = useState(true);
    const [messages, setMessages] = useState([]);

    useEffect(() => {
        (async () => {
            const res = await fetch("/api/user/inbox/messages?conversationId=" + conversationId.id);
            setMessages(await res.json());
            setLoading(false);
        })();
    }, [conversationId]);

    if (loading) {
        return <div>Loading messages...</div>
    }

    return (
        <div>
            {messages.map((m) => (
                <>
                    <h4>{m.senderName} - {m.messageDate}</h4>
                    <p>{m.messageText}</p>
                </>
            ))}
        </div>
    );
}


function App() {
    return (
        <div className="App">
            <h1 id="app-title">I Seek You</h1>

            <ListUsers/>
        </div>
    );
}

export default App;