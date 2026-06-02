import React, { useState, useEffect } from 'react';
import '../Styles/EditarDatos.css';

const API_9 = import.meta.env.VITE_API_9;   
const API_10 = import.meta.env.VITE_API_10; 

export default function EditarDatos() {
    const [nameUser, setNameUser] = useState("");
    const [surnameUser, setSurnameUser] = useState("");
    const [cellphoneUser, setCellphoneUser] = useState("");

    useEffect(() => {
        const email = localStorage.getItem("useremail");

        fetch(`${API_9}/${email}`, {
            method: "GET",
            credentials: "include"
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`Error ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                setNameUser(data.nameUser || "");
                setSurnameUser(data.surnameUser || "");
                setCellphoneUser(data.cellphoneUser || "");
            })
            .catch(err => {
                console.error("Fallo al obtener los datos del usuario:", err);
            });
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const email = localStorage.getItem("useremail");

        try {
            const response = await fetch(`${API_10}/${email}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
                body: JSON.stringify({
                    nameUser: nameUser,
                    surnameUser: surnameUser,
                    cellphoneUser: cellphoneUser
                })
            });

            if (!response.ok) {
                throw new Error(`Error al actualizar: ${response.status}`);
            }
            alert("Cambios guardados exitosamente.");

        } catch (error) {
            alert("Hubo un error al guardar los cambios.");
        }
    }

   return (
    <>
  <div className="container">
    <h1 className="title">Editar Datos</h1>

    <form className="editar-card" onSubmit={handleSubmit}>
      <div className="editar-field">
        <label>Nombre</label>
        <input type="text" placeholder="Tu nombre"
          value={nameUser} onChange={(e) => setNameUser(e.target.value)} />
      </div>
      <div className="editar-field">
        <label>Apellido</label>
        <input type="text" placeholder="Tu apellido"
          value={surnameUser} onChange={(e) => setSurnameUser(e.target.value)} />
      </div>
      <div className="editar-field">
        <label>Celular</label>
        <input type="text" placeholder="+56 9 xxxx xxxx"
          value={cellphoneUser} onChange={(e) => setCellphoneUser(e.target.value)} />
      </div>
      <button type="submit">Guardar Cambios</button>
    </form>
  </div>
    </>
)};