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
    const [mensajeExito, setMensajeExito] = useState(null);

    const manejarSubmit = async (e) => {
        e.preventDefault();
        setErrores(null);
        setMensajeExito(null);


const InvalidNumbers=("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
const InvalidCharacters=["@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "+", "=", "{", "}", "[", "]", "|", "\\", ":", ";", "\"", "'", "<", ">", ",", ".", "?","/"]
const EmailContains=["@gmail.com", "@hotmail.com" ,"@outlook.com"]


//Apartado de validaciones para el formulario de registro


    if(nombre.length < 3){
        setErrores("El nombre debe tener al menos 3 caracteres");
        return;
    }

    if(apellido.length < 3){
        setErrores("El apellido debe tener al menos 3 caracteres");
        return;
    }

    if (email.length < 6 || !EmailContains.some(domain => email.endsWith(domain))) {
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

    if( nombre.includes(InvalidNumbers) ||
        nombre.includes(InvalidCharacters)){
        setErrores("El nombre no puede contener números ni caracteres especiales");
        return;
    }

    if( apellido.includes(InvalidCharacters) ||
        apellido.includes(InvalidNumbers) ){
        setErrores("El apellido no puede contener números ni caracteres especiales");
        return;
    }
       
    if(telefono.includes(InvalidCharacters)){
    setErrores("El teléfono no puede contener caracteres especiales");
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

            const text = await response.text();

            if (response.ok) {
                setMensajeExito("Cuenta creada correctamente");
            } else {
                let mensajeError = "Error al crear la cuenta";

                try {
                    if (text) {
                        const errorData = JSON.parse(text);
                        mensajeError = errorData.message || errorData.error || mensajeError;
                    }
                } catch (parseError) {
                    if (text && text.length < 100) {
                        mensajeError = text;
                    }
                }
                
                setErrores(mensajeError);
            }
        } catch (error) {
            console.error("Error en crear cuenta:", error);
            setErrores("Error de conexión con el servidor");
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
                
                {errores && <p className="error-message" style={{ color: 'red' }}>{errores}</p>}
                {mensajeExito && <p className="success-message" style={{ color: 'green' }}>{mensajeExito}</p>}
            </form>
        </div>
    );
}
