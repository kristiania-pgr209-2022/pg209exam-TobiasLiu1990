import './App.css'
import {useEffect, useState} from "react";

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
        let userId = parseInt(e.target.value);

        if (userId) {
            console.log("User id: " + userId);
            showConversationForUser(userId)
        } else {
            console.log("Just for testing: User does not exist if gets here. Should not happen");
        }
    }

    //the empty <option></option> below is to bypass a first select. Would otherwise not allow to pick user 1.
    return (
        <div id="show-users-droplist">
            <h3>User list</h3>

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
function showConversationForUser(userId) {
    const [loading, setLoading] = useState(true);
    const [conversation, setConversation] = useState([]);


    console.log(userId);
}

function App() {
    return (
        <div className="App">

            <h1>I Seek You</h1>


            <ListUsers/>
        </div>
    );
}

export default App;
