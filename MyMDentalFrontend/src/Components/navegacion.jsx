import React, { useEffect, useState, useRef } from 'react';
import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import '../Styles/navegacion.css';
import logoImagen from '../assets/Imagenes/Logomym.png';

export default function Navegacion() {
  const [mostrarMenu, setMostrarMenu] = useState(false);
  const [categorias, setCategorias] = useState([]);
  const timeoutRef = useRef(null); //  guarda el timer

  useEffect(() => {
    fetch('http://localhost:8080/MyMDentalCommerce/departments/getDepartments')
      .then(response => response.json())
      .then(data => setCategorias(data))
      .catch(error => console.error("Error cargando categorías:", error));
  }, []);

  const handleMouseEnter = () => {
    clearTimeout(timeoutRef.current); // cancela el cierre si el cursor vuelve
    setMostrarMenu(true);
  };

  const handleMouseLeave = () => {
    timeoutRef.current = setTimeout(() => {
      setMostrarMenu(false);
    }, 200); // 200ms de gracia para cruzar al menú
  };

  return (
    <nav className="navbar navbar-expand-lg fixed-top shadow-sm">
      <div className="container-fluid">
        <Link className="navbar-brand logo" to="/">
          <img src={logoImagen} alt="M&M Dental" />
        </Link>

        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarSupportedContent"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div className="collapse navbar-collapse" id="navbarSupportedContent">
          <ul className="navbar-nav me-auto mb-2 mb-lg-0">

            <li className="nav-item">
              <Link className="nav-link" to="/administrador">
                Admin
              </Link>
            </li>

            <li className="nav-item">
              <Link className="nav-link" to="/trabajador">
                Trabajador
              </Link>
            </li>

            <li className="nav-item">
              <Link className="nav-link" to="/Inicio_sesion">
                Inicio De Sesion
              </Link>
            </li>

            <li className="nav-item">
              <Link className="nav-link" to="/carrito">
                Carrito
              </Link>
            </li>


            <li className="nav-item">
              <Link className="nav-link" to="/Nosotros">
                Nosotros
              </Link>
            </li>


            <li
              className="nav-item position-static"
              onMouseEnter={handleMouseEnter}  //  cancela el timer
              onMouseLeave={handleMouseLeave}  //  inicia el timer
            >
              <span className="nav-link" style={{ cursor: 'pointer' }}>
                Categorías ▼
              </span>
              {mostrarMenu && (
                <div
                  className="mega-menu-content"
                  onMouseEnter={handleMouseEnter}  //  mantiene abierto dentro del menú
                  onMouseLeave={handleMouseLeave}  //  cierra al salir del menú
                >
                  {categorias.map(categoria => (
                    <div className="columna-categoria" key={categoria.nameDepartment}>
                      <Link to={`/categoria/${encodeURIComponent(categoria.nameDepartment)}`}>
                        {categoria.nameDepartment}
                      </Link>
                    </div>
                  ))}
                </div>
              )}
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}