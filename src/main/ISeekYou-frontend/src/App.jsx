import './App.css'
import * as React from "react";
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

    return (
        <div>
            <h3>User list</h3>

            <select>
                {user.map((u) => (
                  <>
                    <option>{u.fullName}</option>
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
  );
}

export default App
