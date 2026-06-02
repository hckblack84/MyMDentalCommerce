

import {getUsers} from "../Service/GetUserservice";
import {useEffect, useState} from "react";

export function useUsersState() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const [errorBody, setErrorBody] = useState(null);

  useEffect(() => {
    getUsers()
      .then((result) => setUsers(result))
      .catch((err) => {
        setError(true);
        setErrorBody(err.body ?? {
          code: err.code ?? err.status,
          message: err.message,
        });
        console.error(err);
      })
      .finally(() => setLoading(false));
  }, []);

  return { users, loading, error, errorBody };
}   