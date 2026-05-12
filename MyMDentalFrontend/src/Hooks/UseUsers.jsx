import { getUsers } from "../Service/GetUserService";

export function useUsersState() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);
  const [errorBody, setErrorBody] = useState(null);

  const fetchUsers = () => {
    setLoading(true);
    setError(false);
    setErrorBody(null);
    setUsers([]);

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
    
    };  
}