import { useUsersState } from '../Hooks/UseUser';
import Loader from "./Loader";

export default function Users() {

  const { users, loading, error, errorBody } = useUsersState();

  if (loading) {
    return (
      <Loader entity='Usuarios'/>
    )
  }

  if (error) {
    const status = errorBody?.code ?? "Desconocido"
    const message =
      errorBody?.message ?? "Unknown"

    return (
      <>
        <h1>Ocurrió un error, por favor inténtalo de nuevo</h1>
        <p>code: {errorBody?.code ?? "Unknown"}</p>
        <p>message: {errorBody?.message ?? "Unknown"}</p>
      </>
    )
  }

  return (
    <>
      <h1 className="text-center">Usuarios</h1>
      <div className="row row-cols-1 row-cols-md-4 g-4 container mx-auto">
        {users.map((user, index) => (
          <div className="col" key={user.emailUser ?? index}>
            <div className="card h-100 border border-black shadow-sm">
              <div className="card-body">
                <h5 className="card-title">{user.nameUser} {user.surnameUser}</h5>
                <p className="card-text">Email: {user.emailUser}</p>
                <p className="card-text">Teléfono: {user.cellphoneUser}</p>
                <p className="card-text">Rol: {user.role}</p>
              </div>
            </div>
          </div>
        ))}
      </div>
    </>
  )
}