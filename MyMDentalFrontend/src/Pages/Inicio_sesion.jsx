import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "../Styles/Inicio_sesion.css"

export default function InicioSesion() {

  const [correo, setCorreo] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('http://localhost:8080/MyMDentalCommerce/session/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        withCredentials: true,
        credentials: "include",
        body: JSON.stringify({ 
          emailUser: correo,  
          password: password
        })
      });

      if (!response.ok) {
        throw new Error('Credenciales incorrectas');
      }

      const data = await response.json();
      console.log("Respuesta:", data);

      localStorage.setItem("role", data.role);
      navigate('/');


      const perfilResponse = await fetch('http://localhost:8080/MyMDentalCommerce/session/perfil', {
        credentials: "include"
      });

      if (perfilResponse.ok) {
        const perfilData = await perfilResponse.text();
        console.log("Perfil:", perfilData);
      } else {
        console.error('Error al obtener perfil: No autenticado');
      }

    } catch (error) {
      console.error('Error:', error);
      alert('Error al iniciar sesión: ' + error.message);
    } 


  };

  return (
    <>
      <div className='login-page'>
        <form onSubmit={handleSubmit} className='form-container'>
          <div className='form-title'>Inicio de sesión</div>

          <input 
            className='form-input'
            type="text" 
            placeholder="correo" 
            value={correo} 
            onChange={(e) => setCorreo(e.target.value)} 
          />

          <input 
            className='form-input'
            type="password" 
            placeholder="password" 
            value={password} 
            onChange={(e) => setPassword(e.target.value)} 
          />

          <button type="submit" className="form-button">
            Iniciar Sesión
          </button>

          <p className='register-link'>
            ¿No tienes una cuenta? <a href="/crear_cuenta">Crear cuenta</a>
          </p>

        </form>
      </div>
    </>
  );
}