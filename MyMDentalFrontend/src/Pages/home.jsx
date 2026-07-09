import React from "react";
import carrusel1 from "../assets/Imagenes/Carrusel_1.png";
import carrusel2 from "../assets/Imagenes/Carrusel_2.png";
import carrusel3 from "../assets/Imagenes/Carrusel_3.png";
import carrusel4 from "../assets/Imagenes/Carrusel_4.png";
import carrusel5 from "../assets/Imagenes/Carrusel_5.png";
import marca3m from "../assets/Imagenes/Marcas/3M.png";
import marcaBisco from "../assets/Imagenes/Marcas/bisco.webp";
import marcaDentsply from "../assets/Imagenes/Marcas/debtSply.png";
import marcaKerr from "../assets/Imagenes/Marcas/kerr.jpg";
import marcaLogo1 from "../assets/Imagenes/Marcas/logo1.jpg";
import marcaMaquira from "../assets/Imagenes/Marcas/maquira.png";
import marcaMicrodont from "../assets/Imagenes/Marcas/Microdont.png";
import marcaSkydent from "../assets/Imagenes/Marcas/Skydent.png";
import Productos from "../Components/Productos";
import "../Styles/homeStyle.css";


export default function Home() {

    return (
        <>
        <h1 className="Encabezado">Bienvenido a MYM DENTAL</h1>

        <div id="carouselExample" className="carousel slide" data-bs-ride="carousel" data-bs-interval="3000">
            <div className="carousel-indicators">
                <button type="button" data-bs-target="#carouselExample" data-bs-slide-to="0" className="active" aria-current="true"></button>
                <button type="button" data-bs-target="#carouselExample" data-bs-slide-to="1"></button>
                <button type="button" data-bs-target="#carouselExample" data-bs-slide-to="2"></button>
                <button type="button" data-bs-target="#carouselExample" data-bs-slide-to="3"></button>
                <button type="button" data-bs-target="#carouselExample" data-bs-slide-to="4"></button>
            </div>
            <div className="carousel-inner">
                <div className="carousel-item active">
                <img src={carrusel1} className="d-block w-100" alt="Carrusel 1"/>
                </div>
                <div className="carousel-item">
                <img src={carrusel2} className="d-block w-100" alt="Carrusel 2"/>
                </div>
                <div className="carousel-item">
                <img src={carrusel3} className="d-block w-100" alt="Carrusel 3"/>
                </div>
                <div className="carousel-item">
                <img src={carrusel4} className="d-block w-100" alt="Carrusel 4"/>
                </div>
                <div className="carousel-item">
                <img src={carrusel5} className="d-block w-100" alt="Carrusel 5"/>
                </div>
            </div>
            <button className="carousel-control-prev" type="button" data-bs-target="#carouselExample" data-bs-slide="prev">
                <span className="carousel-control-prev-icon" aria-hidden="true"></span>
                <span className="visually-hidden">Previous</span>
            </button>
            <button className="carousel-control-next" type="button" data-bs-target="#carouselExample" data-bs-slide="next">
                <span className="carousel-control-next-icon" aria-hidden="true"></span>
                <span className="visually-hidden">next</span>
            </button>
        </div>

        <div className="mid">
            <div className="marca-card">
                <img src={marca3m} alt="3M"/>
                <span className="marca-nombre">3M</span>
            </div>
            <div className="marca-card">
                <img src={marcaBisco} alt="Bisco"/>
                <span className="marca-nombre">Bisco</span>
            </div>
            <div className="marca-card">
                <img src={marcaDentsply} alt="Dentsply"/>
                <span className="marca-nombre">Dentsply</span>
            </div>
            <div className="marca-card">
                <img src={marcaKerr} alt="Kerr"/>
                <span className="marca-nombre">Kerr</span>
            </div>
            <div className="marca-card">
                <img src={marcaLogo1} alt="Logo"/>
                <span className="marca-nombre">Logo</span>
            </div>
            <div className="marca-card">
                <img src={marcaMaquira} alt="Maquira"/>
                <span className="marca-nombre">Maquira</span>
            </div>
            <div className="marca-card">
                <img src={marcaMicrodont} alt="Microdont"/>
                <span className="marca-nombre">Microdont</span>
            </div>
            <div className="marca-card">
                <img src={marcaSkydent} alt="Skydent"/>
                <span className="marca-nombre">Skydent</span>
            </div>
        </div>
        <h1 className="encabezadoHome"> Nuestros productos </h1>

        <Productos isFiltered={false} />

    </>
    );








}