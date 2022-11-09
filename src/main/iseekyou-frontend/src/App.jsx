import './App.css'
import React, {useEffect, useState} from "react";

let userId = "";

//Shows all users. Sets userId.
function ListUsers() {
    const [loading, setLoading] = useState(true);
    const [user, setUser] = useState([]);

    useEffect(() => {
        (async () => {
            const res = await fetch("/api/user");
            setUser(await res.json());
            setLoading(false);
        })();
    }, []);

    if (loading) {
        return <div>Loading user...</div>
    }


    function handleChange(e) {
        userId = parseInt(e.target.value);

        if (userId) {
            console.log("User id: " + userId);
            // ShowConversationForUser(userId); //Would like to pass, but breaks hooks rules....
        } else {
            console.log("Just for testing: User does not exist if gets here. Should not happen");
        }
    }

    //the empty <option></option> below is to bypass a first select. Would otherwise not allow to pick user 1.
    return (
        <div id="show-users-droplist">
            <h2>User list</h2>

            <select value={user} onChange={handleChange}>
                <option id="first-option">Select a user to view conversation and messages</option>

                {user.map((u) => (
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
    console.log(userId);

    useEffect(() => {
        (async () => {
            const res = await fetch(`/api/user/inbox=${userId}`);
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
                        <li key={c.id}>c.title</li>
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
