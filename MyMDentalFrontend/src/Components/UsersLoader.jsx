import React from "react";
import "../Styles/Loader.css";
import {useUsersState} from '../Hooks/UseUsers';


export default function UsersLoader({ entity = "" }) {
  const { loading } = useUsersState();
    if (!loading) return null;
  return (
    <div style={{ display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "center" }}>
      <div className="loader"></div>

      <h1>Tittle</h1>
      <div>Cargando {entity}...</div>
    </div>
  );
}   