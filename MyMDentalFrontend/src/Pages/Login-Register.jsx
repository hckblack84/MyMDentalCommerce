import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../Styles/Login-Register.css";

const InvalidNumbers = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"];
const InvalidCharacters = ["@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "+", "=", "{", "}", "[", "]", "|", "\\", ":", ";", "\"", "'", "<", ">", ",", ".", "?", "/"];
const EmailContains = ["@gmail.com", "@hotmail.com", "@outlook.com"];

export default function LoginRegister() {
  const [modo, setModo] = useState("login");
  const [animClass, setAnimClass] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [password2, setPassword2] = useState("");
  const [telefono, setTelefono] = useState("");
  const [error, setError] = useState(null);



  const toggleModo = (nuevoModo) => {
    if (nuevoModo === modo || animClass) return;
    const slideOut = modo === "login" ? "slide-out-left" : "slide-out-right";
    setAnimClass(slideOut);
    setTimeout(() => {
      setModo(nuevoModo);
      setError(null);
      setEmail("");
      setPassword("");
      setNombre("");
      setApellido("");
      setPassword2("");
      setTelefono("");
      const slideIn = nuevoModo === "login" ? "slide-in-left" : "slide-in-right";
      setAnimClass(slideIn);
      setTimeout(() => setAnimClass(""), 300);
    }, 200);
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setError(null);
    if (!email.trim()) {
      setError("El correo es obligatorio");
      return;
    }
    if (!password) {
      setError("La contraseña es obligatoria");
      return;
    }
    setLoading(true);
    try {
      const response = await fetch(import.meta.env.VITE_API_USER_LOGIN, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ emailUser: email, password }),
      });
      if (!response.ok) throw new Error("Credenciales incorrectas");
      const data = await response.json();
      localStorage.setItem("role", data.role);
      localStorage.setItem("useremail", data.useremail);
      localStorage.setItem("token", data.token);
      navigate("/");
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setError(null);

    if (nombre.length < 3) {
      setError("El nombre debe tener al menos 3 caracteres");
      return;
    }
    if (apellido.length < 3) {
      setError("El apellido debe tener al menos 3 caracteres");
      return;
    }
    if (email.length < 6) {
      setError("El email debe tener al menos 6 caracteres");
      return;
    }
    if (!EmailContains.some((domain) => email.endsWith(domain))) {
      setError("El email no es válido");
      return;
    }
    if (password.length < 6) {
      setError("La contraseña debe tener al menos 6 caracteres");
      return;
    }
    if (password !== password2) {
      setError("Las contraseñas no coinciden");
      return;
    }
    if (telefono.length !== 9) {
      setError("El teléfono debe tener 9 dígitos");
      return;
    }
    if (
      InvalidNumbers.some((num) => nombre.includes(num)) ||
      InvalidCharacters.some((char) => nombre.includes(char))
    ) {
      setError("El nombre no puede contener números ni caracteres especiales");
      return;
    }
    if (
      InvalidCharacters.some((char) => apellido.includes(char)) ||
      InvalidNumbers.some((num) => apellido.includes(num))
    ) {
      setError("El apellido no puede contener números ni caracteres especiales");
      return;
    }
    if (InvalidCharacters.some((char) => telefono.includes(char))) {
      setError("El teléfono no puede contener caracteres especiales");
      return;
    }

    setLoading(true);
    try {
      const response = await fetch(import.meta.env.VITE_API_USER_REGISTER, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          nameUser: nombre,
          surnameUser: apellido,
          emailUser: email,
          passwordUser: password,
          cellphoneUser: telefono,
          role: "CLIENT"
        }),
      });
      if (!response.ok) throw new Error("Error al registrar");
      alert("Cuenta creada correctamente");
      toggleModo("login");
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <div className="auth-container">
        <div className="auth-tabs">
          <button
            className={`auth-tab ${modo === "login" ? "active" : ""}`}
            onClick={() => toggleModo("login")}
          >
            Iniciar Sesión
          </button>
          <button
            className={`auth-tab ${modo === "register" ? "active" : ""}`}
            onClick={() => toggleModo("register")}
          >
            Crear Cuenta
          </button>
        </div>

        <div className={`auth-body ${animClass}`}>
          {modo === "login" ? (
            <form onSubmit={handleLogin}>
              <input
                className="auth-input"
                type="text"
                placeholder="Correo electrónico"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
              <input
                className="auth-input"
                type="password"
                placeholder="Contraseña"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
              {error && <p className="auth-error">{error}</p>}
              <button
                type="submit"
                className={`auth-button ${loading ? "loading" : ""}`}
                disabled={loading}
              >
                {loading ? "Cargando..." : "Iniciar Sesión"}
              </button>
              <p className="auth-toggle">
                ¿No tienes una cuenta?{" "}
                <button
                  type="button"
                  className="auth-toggle-link"
                  onClick={() => toggleModo("register")}
                >
                  Crear cuenta
                </button>
              </p>
            </form>
          ) : (
            <form onSubmit={handleRegister}>
              <div className="auth-grid">
                <input
                  className="auth-input"
                  type="text"
                  placeholder="Nombre"
                  value={nombre}
                  onChange={(e) => setNombre(e.target.value)}
                />
                <input
                  className="auth-input"
                  type="text"
                  placeholder="Apellido"
                  value={apellido}
                  onChange={(e) => setApellido(e.target.value)}
                />
              </div>
              <input
                className="auth-input"
                type="email"
                placeholder="Correo electrónico"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
              <div className="auth-grid">
                <input
                  className="auth-input"
                  type="password"
                  placeholder="Contraseña"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
                <input
                  className="auth-input"
                  type="password"
                  placeholder="Repetir contraseña"
                  value={password2}
                  onChange={(e) => setPassword2(e.target.value)}
                />
              </div>
              <input
                className="auth-input"
                type="number"
                placeholder="Teléfono (9 dígitos)"
                value={telefono}
                onChange={(e) => setTelefono(e.target.value)}
              />
              {error && <p className="auth-error">{error}</p>}
              <button
                type="submit"
                className={`auth-button ${loading ? "loading" : ""}`}
                disabled={loading}
              >
                {loading ? "Cargando..." : "Registrarse"}
              </button>
              <p className="auth-toggle">
                ¿Ya tienes una cuenta?{" "}
                <button
                  type="button"
                  className="auth-toggle-link"
                  onClick={() => toggleModo("login")}
                >
                  Iniciar sesión
                </button>
              </p>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}
