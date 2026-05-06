import React, {useEffect, useState} from "react";
import "../Styles/Crear_cuenta.css";


export default function Crear_cuenta() {
    const [nombre, setNombre] = useState("");
    const [apellido, setApellido] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [password2, setPassword2] = useState("");
    const [telefono, setTelefono] = useState("");
    const [rol, setRol] = useState("CLIENT");
    const [errores, setErrores] = useState(null);

const manejarSubmit = async (e) => {
    e.preventDefault();
    setErrores("");

    if(nombre.length < 3){
        setErrores("El nombre debe tener al menos 3 caracteres");
        return;
    }

    if(apellido.length < 3){
        setErrores("El apellido debe tener al menos 3 caracteres");
        return;
    }

    if (!email.includes("@gmail.com") && !email.includes("@hotmail.com")) {
        setErrores("El email no es válido");
        return;
    }

    if (email.length < 6){
        setErrores("El email debe tener al menos 6 caracteres");
        return;
    }

    if(password.length < 6){
        setErrores("La contraseña debe tener al menos 6 caracteres");
        return;
    }

    if(password !== password2){
        setErrores("Las contraseñas no coinciden");
        return;
    }

    if (telefono.length !== 9) {
        setErrores("El teléfono debe tener 9 dígitos");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/MyMDentalCommerce/session/register", {
            method: 'POST',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                nameUser: nombre,
                surnameUser: apellido,
                emailUser: email,
                passwordUser: password,
                cellphoneUser: telefono,
                role: rol
            })
        });

        if (!response.ok) {
            throw new Error("Error al registrar");
        }

        alert("Cuenta creada correctamente");

    } catch (error) {
        setErrores(error);
    }
};


    return (
   <div className="register-page"> 
        <form onSubmit={manejarSubmit} className="form-container">
            <h2 className="form-title">Crear Cuenta</h2>

            <div className="form-grid">
                <input 
                    className="form-input"
                    type="text" 
                    placeholder="Nombre" 
                    value={nombre} 
                    onChange={(e) => setNombre(e.target.value)} 
                />
                <input 
                    className="form-input"
                    type="text" 
                    placeholder="Apellido" 
                    value={apellido} 
                    onChange={(e) => setApellido(e.target.value)} 
                />
            </div>

            <input 
                className="form-input"
                type="email" 
                placeholder="Email" 
                value={email} 
                onChange={(e) => setEmail(e.target.value)} 
            />

            <div className="form-grid">
                <input 
                    className="form-input"
                    type="password" 
                    placeholder="Contraseña" 
                    value={password} 
                    onChange={(e) => setPassword(e.target.value)} 
                />
                <input 
                    className="form-input"
                    type="password" 
                    placeholder="Repetir Contraseña" 
                    value={password2} 
                    onChange={(e) => setPassword2(e.target.value)} 
                />
            </div>

            <input 
                className="form-input"
                type="number"
                placeholder="Teléfono (9 dígitos)" 
                value={telefono}
                onChange={(e) => setTelefono(e.target.value)} 
            />

            <button type="submit" className="form-button">Registrarse</button>
            
            {errores && <p className="error-message">{errores}</p>}
        </form>
    </div>
  )
}

