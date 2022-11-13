import './App.css'
import * as React from "react";
import {useEffect, useState} from "react";

/*
    -list users
        -show conversation titles
            - show messages in conversation
            - write new message
    -user settings
    -new conversation for user
 */

//Global use to easily track current user logged in.
let currentUserId = 0;

//Shows all users
function ListUsers() {
    const [loading, setLoading] = useState(true);
    const [users, setUsers] = useState([]);
    const [fake, setFake] = useState(0);    //Used to pass user id to ShowConversationForUser()

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
        let currentUser = JSON.parse(e.target.value);
        currentUserId = currentUser.id;

        //Set user settings + show user settings
        ApplyUserSettings(currentUser.fullName, currentUser.color)

        //Not used - crash if deleted????
        setFake(currentUser.id);
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
                        <option key={u.id} value={JSON.stringify(u)}>{u.id} {u.fullName} {u.email}</option>
                    ))}
                </select>
            </div>

            <div id="conversations">
                <ShowConversationForUser/>
            </div>

            <div id="user-settings" style={{visibility: 'hidden'}}>
                <UpdateUserSettings/>

                {/*<UserSettingsName/>*/}
                {/*<UserSettingsEmail/>*/}
                {/*<UserSettingsFavoriteColor/>*/}
            </div>
        </>
    );
}

function ApplyUserSettings(name, color) {
    let userName = document.getElementById("selected-user").innerHTML = name;
    let userColor = document.getElementById("app-title").style.color = color;
    document.getElementById("user-settings").style.visibility = 'visible';
}

function UpdateUserSettings() {
    const [fullName, setFullName] = useState("");
    const [email, setEmail] = useState("");
    const [color, setColor] = useState("");

    async function handleSubmit(e) {
        e.preventDefault();

        if (currentUserId === 0) {
            return
        }
        await fetch("/api/user/settings?userId=" + currentUserId, {
            method: "post",
            body: JSON.stringify({fullName, email, color}),
            headers: {
                "Content-Type": "application/json",
            },
        });
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>
                        New name:{" "}
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

// function UserSettingsName() {
//     const [fullName, setFullName] = useState("");
//
//     async function handleSubmit(e) {
//         e.preventDefault();
//
//         if (currentUserId === 0) {
//             return;
//         }
//         await fetch("/api/user/settings/changename?userId=" + currentUserId, {
//             method: "post",
//             body: JSON.stringify({fullName}),
//             headers: {
//                 "Content-Type": "application/json",
//             },
//         });
//     }
//
//     return (
//         <div>
//             <form onSubmit={handleSubmit}>
//                 <label>
//                     New name:{" "}
//                     <input
//                         type="text"
//                         value={fullName}
//                         onChange={(e) => setFullName(e.target.value)}
//                     />
//                 </label>
//                 <button>Submit</button>
//             </form>
//         </div>
//     );
// }

// function UserSettingsEmail() {
//     const [email, setEmail] = useState("");
//
//     async function handleSubmit(e) {
//         e.preventDefault();
//
//         if (currentUserId === 0) {
//             return;
//         }
//         await fetch("/api/user/settings/changeemail?userId=" + currentUserId, {
//             method: "post",
//             body: JSON.stringify({email}),
//             headers: {
//                 "Content-Type": "application/json",
//             },
//         });
//     }
//
//     return (
//         <div>
//             <form onSubmit={handleSubmit}>
//                 <label>
//                     New E-mail Address:{" "}
//                     <input
//                         type="text"
//                         value={email}
//                         onChange={(e) => setEmail(e.target.value)}
//                     />
//                 </label>
//                 <button>Submit</button>
//             </form>
//         </div>
//     );
// }

// function UserSettingsFavoriteColor() {
//     const [color, setColor] = useState("");
//
//     async function handleSubmit(e) {
//         e.preventDefault();
//
//         if (currentUserId === 0) {
//             return;
//         }
//         await fetch("/api/user/settings/changecolor?userId=" + currentUserId, {
//             method: "post",
//             body: JSON.stringify({color}),
//             headers: {
//                 "Content-Type": "application/json",
//             },
//         });
//     }
//
//     return (
//         <div>
//             <form onSubmit={handleSubmit}>
//                 <label>
//                     New favorite color:{" "}
//                     <input
//                         type="text"
//                         value={color}
//                         onChange={(e) => setColor(e.target.value)}
//                     />
//                 </label>
//                 <button>Submit</button>
//             </form>
//         </div>
//     );
// }

//Get all conversations for user.
function ShowConversationForUser() {
    const [loading, setLoading] = useState(true);
    const [conversation, setConversation] = useState([]);
    const [conversationId, setConversationId] = useState(0);

    useEffect(() => {
        if (currentUserId === 0) {
            return;
        }

        (async () => {
            const res = await fetch("/api/user/inbox?userId=" + currentUserId);
            setConversation(await res.json());
            setLoading(false);
        })();
    }, [currentUserId]);

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

            <div id="new-conversation">
                <CreateNewConversation/>
            </div>
        </div>
    );
}

/*
    1. Table Conversation
        Title finished in CreateConversationTitle
    2. Need to POST:
        Table Conversation_members:
            Newest conversation ID
            user ID
    3. Need to POST:
        Table Messages:
            conversation_id (fk)
            sender_id (user_id)
            date (auto)
            content = message
 */

function CreateNewConversation() {
    const [conversationTitle, setConversationTitle] = useState("");
    const [conversationId, setConversationId] = useState(0);

    //Should POST a conversation object (title)
    //Method does also return id.
    async function handleSubmitConversationTitle(e) {
        e.preventDefault();

        const res = await fetch("api/user/inbox/new", {
            method: "post",
            body: JSON.stringify({conversationTitle}),
            headers: {
                "Content-Type": "application/json",
            },
        })
        setConversationId(await res.json());
        console.log("New conversation title as parameter: " + conversationTitle);
    }

    // This should be for handling who to add to a conversation
    // Use the recipientList
    //POST recipients
    function handleSubmitRecipients(e) {
        e.preventDefault()

    }

    //For message
    //Post message
    function handleSubmitMessage(e) {
        e.preventDefault();

    }

    return (
        <div>
            <h2>New conversation</h2>

            <div id="new-conversation-div">
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
            <hr></hr>

            <div>
                <form onSubmit={handleSubmitRecipients}>
                    <AddConversationMembers/>
                    <button>Submit recipients</button>
                </form>
            </div>
            <hr></hr>

            <div>
                <form onSubmit={handleSubmitMessage}>
                    <CreateMessage/>
                    <button>Submit message</button>
                </form>
            </div>

        </div>
    )
}

//Create new conversation - WORK
// async function CreateNewConversationTitle() {
//     const [conversationTitle, setConversationTitle] = useState("");
//     const [conversationId, setConversationId] = useState(0);
//
//     async function handleSubmit(e) {
//         e.preventDefault();
//
//         const res = await fetch("api/user/inbox/new", {
//             method: "post",
//             body: JSON.stringify({conversationTitle}),
//             headers: {
//                 "Content-Type": "application/json",
//             },
//         })
//         setConversationId(await res.json());
//         console.log("New conversation title as parameter: " + conversationTitle);
//     }
//     // .then(response => response.json())
//     // .then(conversation => setConversationId(conversation.id))
//
//     return (
//         <div id="new-conversation-div">
//             <form onSubmit={handleSubmit}>
//                 <label>
//                     Conversation title:
//                     <input type="text"
//                            value={conversationTitle}
//                            onChange={(e) => setConversationTitle(e.target.value)}
//                     />
//                 </label>
//                 <button>Submit conv</button>
//             </form>
//         </div>
//     )
// }


// Find all except current user - WORKS
function FindConversationUsers() {
    const [users, setUsers] = useState([]); //All users except current

    useEffect(() => {
        if (currentUserId === 0) {
            return;
        }

        const fetchUsers = async () => {
            const res = await fetch("/api/user/inbox/new/conversationMembers?userId=" + currentUserId);
            setUsers(await res.json());

            // console.log("FindConversationUsers() - Should show list of users: " +);
            // return users;
        }
        fetchUsers()
            .catch(console.error);
        console.log(fetchUsers());
    }, []);

    return users;
}


// Add users to array
let recipientList = [];

function AddConversationMembers() {
    let users = FindConversationUsers();

    function handleClick(e) {
        document.getElementById(e.target.value).style.visibility = 'hidden';
        recipientList.push(e.target.value);
        console.log("recipientList: " + recipientList)
    }

    return (
        <div>
            <h4>Recipients: </h4>

            {users.map((u) => (
                <button id={u.id} value={u.id} onClick={handleClick}>{u.email}</button>
            ))}
        </div>
    )
}

function CreateMessage() {
    return (
        <label>
            Message:
            <input type="text">

            </input>
        </label>
    )
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