import './App.css'
import * as React from "react";
import {useEffect, useState} from "react";

function ListUsers() {
    const [user, setUser] = useState([]);

    useEffect(() => {
        (async () => {
            const res = await fetch("/api/user");
            setUser(await res.json());
        })();
    }, []);

    return (
        <div id="user-list">
            <h3>User list</h3>

            <select>
                {user.map((u) => (
                  <>
                    <option value={u.fullName}></option>
                  </>
                ))}
            </select>
        </div>
    );
}

function App() {
  return (
    <div className="App">

      <h1>I Seek You </h1>

    <ListUsers/>
    </div>
  )
}

export default App
