import './App.css'
import React, {useEffect, useState} from "react";

let userId = 0;

//Shows all users. Sets userId.
function ListUsers() {
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


    function handleChange(e) {
        userId = parseInt(e.target.value);
        console.log("User id: " + userId);

        if (userId) {
            // ShowConversationForUser(userId); //Would like to pass, but breaks hooks rules....
        }
    }

    //the empty <option></option> below is to bypass a first select. Would otherwise not allow to pick user 1.
    return (
        <div id="show-users-droplist">
            <h2>User list</h2>

            <select value={users} onChange={handleChange}>
                <option id="first-option">Select a user to view conversation and messages</option>

                {users.map((u) => (
                    <option key={u.id} value={u.id}>{u.id} {u.fullName} {u.eMail}</option>
                ))}
            </select>
        </div>
    );
}


//New GET method to get all conversations to show for this user.
function ShowConversationForUser(userId) {
    const [loading, setLoading] = useState(true);
    const [conversation, setConversation] = useState([]);
    console.log("asdasd: " + JSON.stringify(userId));

    useEffect(() => {
        (async () => {
            const res = await fetch("/api/user/inbox?id=" + userId);
            setConversation(await res.json());
            setLoading(false);
        })();
    }, []);

    if (loading) {
        return <div>Loading conversations...</div>
    }
    if (userId === "") {
        return;
    } else {
        return (
            <div id="show-all-conversations">
                <h2>Conversations</h2>
                <ul>
                    {conversation.map((c) => (
                        <li key={c.id}>{c.conversationTitle}</li>
                    ))}
                </ul>
            </div>
        );
    }
}


function App() {
    return (
        <div className="App">

            <h1>I Seek You</h1>


            <ListUsers/>
            <ShowConversationForUser/>
        </div>
    );
}

export default App;
