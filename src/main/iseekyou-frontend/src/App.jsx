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
        <div>
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


//Get all conversations for user.
function ShowConversationForUser({user}) {
    const [loading, setLoading] = useState(true);
    const [conversation, setConversation] = useState([]);
    const [conversationId, setConversationId] = useState(0);

    useEffect(() => {
        (async () => {
            const res = await fetch("/api/user/inbox?userId=" + user.id);
            setConversation(await res.json());
            setLoading(false);
        })();
    }, []);

    if (loading) {
        return <div>Loading conversations...</div>
    }

    return (
        <div>
            <h2>Conversations</h2>

            {conversation.map((c) => (
                <div>
                    <button key={c.id} onClick={(e) => setConversationId(parseInt(e.target.value))}
                            value={c.id}>{c.id} - {c.conversationTitle}</button>
                </div>
            ))}
            <div id="show-messages">
                <ShowMessageBox id={conversationId}/>
            </div>

            {/*<div id="new-conversation">*/}
            {/*    <CreateNewConversation/>*/}
            {/*</div>*/}
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

        const res = await fetch("/api/user/inbox/new/conversation", {
            method: "post",
            body: JSON.stringify({conversationTitle}),
            headers: {
                "Content-Type": "application/json",
            },
        })
        setConversationId(await res.json());
        console.log("New conversation title: " + conversationTitle);
    }

    // This should be for handling who to add to a conversation
    // Use the recipientList
    //POST recipients
    async function handleSubmitRecipients(e) {
        e.preventDefault()

        function handleRecipientClick(e) {
            //knappar
            e.preventDefault()
        }

        await fetch("api/user/inbox/new/conversation/addRecipients", {
            method: "post",
            body: JSON.stringify({conversationId, recipientId}),
            headers: {
                "Content-Type": "application/json",
            },
        });
        console.log("New conversation Id: " + conversationId);
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
                    <h4>Recipients: </h4>

                    {users.map((u) => (
                        <button id={u.id} value={u.id} onClick={handleRecipientClick}>{u.email}</button>
                    ))}
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

// Create new conversation - WORK
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


// Function only finds all users except current user - WORKS
// function FindConversationUsers(id) {
//     const [recipients, setRecipients] = useState([]); //All users except current
//
//     useEffect(() => {
//         const fetchUsers = async () => {
//             const res = await fetch("/api/user/inbox/new/conversationRecipients?userId=" + id);
//             setRecipients(await res.json());
//
//             // console.log("FindConversationUsers() - Should show list of users: " +);
//             // return users;
//         }
//         fetchUsers()
//             .catch(console.error);
//         console.log(fetchUsers());
//     }, []);
//
//     return recipient;
// }

// Add users to array
let recipientId;

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

    // let users = FindConversationUsers(user.id);
    // // Should add objects to recipientList (ConversationMembers objects)
    // function handleClick(e) {
    //     setRecipients(e.target.value);
    //
    //     recipientId = (e.target.value);
    //     document.getElementById(e.target.value).style.visibility = 'hidden';
    //
    //     console.log("recipientList: " + recipientId)
    // }

    return recipients;
    // (
    //     <div>
    //         <h4>Recipients: </h4>
    //
    //         {users.map((u) => (
    //             <button id={u.id} value={u.id} onClick={handleClick}>{u.email}</button>
    //         ))}
    //     </div>
    // )
}

function CreateMessage({setMessages}) {
    async function handleSubmit(e) {
        e.preventDefault;
        const res = await fetch("api/newMessage");
        //Append new message to the messages state
        setMessages((oldMessages) => [...oldMessages, res.json()]);
    }

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
    const [user, setUser] = useState();
    const [recipients, setRecipients] = useState();

    return (
        <div className="App">
            <h1 id="app-title">I Seek You</h1>

            <ListUsers user={user} setUser={setUser}/>
            {user && <UpdateUserSettings user={user}/>}
            {user && <ShowConversationForUser user={user}/>}
            {user && <FindRecipientsToAdd user={user} recipient={recipients} setRecipients={setRecipients}/>}
        </div>
    );
}

export default App;