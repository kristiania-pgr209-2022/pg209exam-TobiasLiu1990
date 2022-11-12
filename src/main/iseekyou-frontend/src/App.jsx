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

            <div id="conversations">
                <ShowConversationForUser id={userId}/>
                <FindConversationUsers id={userId}/>
            </div>

            <div id="user-settings" style={{visibility: 'hidden'}}>
                <UserSettingsName id={userId}/>
                <UserSettingsEmail id={userId}/>
                <UserSettingsFavoriteColor id={userId}/>
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
            const res = await fetch("/api/user/setcolor?userColor=" + userId.id);
            setUser(await res.json());
            setLoading(false);
        })();
    }, [userId]);

    if (loading) {
        return <div>Logo-color should change soon.......</div>
    } else {
        document.getElementById("selected-user").innerHTML = user.map(u => u.fullName);
        document.getElementById("app-title").style.color = user.map(u => u.color);

        //Set settings to visible again
        document.getElementById("user-settings").style.visibility = 'visible';
    }
}

function UserSettingsName(userId) {
    const [fullName, setFullName] = useState("");

    async function handleSubmit(e) {
        e.preventDefault();

        if (userId.id === 0) {
            return;
        }
        await fetch("/api/user/settings/changename?userId=" + userId.id, {
            method: "post",
            body: JSON.stringify({fullName}),
            headers: {
                "Content-Type": "application/json",
            },
        });
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <label>
                    New name:{" "}
                    <input
                        type="text"
                        value={fullName}
                        onChange={(e) => setFullName(e.target.value)}
                    />
                </label>
                <button>Submit</button>
            </form>
        </div>
    );
}

function UserSettingsEmail(userId) {
    const [email, setEmail] = useState("");

    async function handleSubmit(e) {
        e.preventDefault();

        if (userId.id === 0) {
            return;
        }
        await fetch("/api/user/settings/changeemail?userId=" + userId.id, {
            method: "post",
            body: JSON.stringify({email}),
            headers: {
                "Content-Type": "application/json",
            },
        });
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <label>
                    New E-mail Address:{" "}
                    <input
                        type="text"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </label>
                <button>Submit</button>
            </form>
        </div>
    );
}

function UserSettingsFavoriteColor(userId) {
    const [color, setColor] = useState("");

    async function handleSubmit(e) {
        e.preventDefault();

        if (userId.id === 0) {
            return;
        }
        await fetch("/api/user/settings/changecolor?userId=" + userId.id, {
            method: "post",
            body: JSON.stringify({color}),
            headers: {
                "Content-Type": "application/json",
            },
        });
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <label>
                    New favorite color:{" "}
                    <input
                        type="text"
                        value={color}
                        onChange={(e) => setColor(e.target.value)}
                    />
                </label>
                <button>Submit</button>
            </form>
        </div>
    );
}

//Get all conversations for user.
function ShowConversationForUser(userId) {
    const [loading, setLoading] = useState(true);
    const [conversation, setConversation] = useState([]);
    const [conversationId, setConversationId] = useState(0);

    useEffect(() => {
        if (userId.id === 0) {
            return;
        }

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

            <div>
                <CreateNewConversation/>
            </div>
        </div>
    );
}



//Create new conversation
function CreateNewConversation() {
    const [conversationTitle, setConversationTitle] = useState("");

    const setId = FindNewConversationId();
    console.log("Id from another method: " + setId.id)

    async function handleSubmit(e) {
        e.preventDefault();

        await fetch("api/user/inbox/new", {
            method: "post",
            body: JSON.stringify({conversationTitle}),
            headers: {
                "Content-Type": "application/json",
            },
        });
    }

    return (
        <div> New conversation
            <form onSubmit={handleSubmit}>
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
    )
}

//Get the newest conversation - used for adding a new conversation.
function FindNewConversationId() {
    const [id, setId] = useState(0);

    useEffect(() => {
        (async () => {
            const res = await fetch("/api/user/inbox/new/conversationId");
            setId(await res.json());

        })();
    }, []);

    return id;
}

// Get all except current user
function FindConversationUsers(userId) {
    const [users, setUsers] = useState([]); //All users except current

    useEffect(() => {
        if (userId.id === 0) {
            return;
        }
        (async () => {
            const res = await fetch("/api/inbox/new?userId=" + userId.id);
            setUsers(await res.json());
        })();
    }, []);

    return users;
}


// function AddConversationMembers(users, conversationId) {
//     console.log("Conv id: " + conversationId.id)
//
//     //////
//     //Now get the Id of the conversation from db
//     if (conversationTitle === "") {
//         return;
//     } else {
//         useEffect(() => {
//             (async () => {
//                 const res = await fetch("/api/user/inbox/new/conversationId");
//                 setId(await res.json());
//             })();
//         }, [conversationTitle]);
//     }
// }

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