import React from "react";
import carrosule from "../assets/Imagenes/carrosule.png";
import carrusel2 from "../assets/Imagenes/carrusel2.png";
import marca1 from "../assets/Imagenes/marca1.png";
import Productos from "../Components/Productos";
import "../Styles/homeStyle.css";


export default function Home() {
    const urlBack = "http://localhost:8080/MyMDentalCommerce/products/adminProducts";

    return (
        <>
        <h1 className="Encabezado">Bienvenido a MYM DENTAL</h1>

        <div id="carouselExample" className="carousel slide">
            <div className="carousel-inner">
                <div className="carousel-item active">
                <img src={carrosule} className="d-block w-100"/>
                </div>
                <div className="carousel-item">
                <img src={carrusel2} className="d-block w-100" />
                </div>
                <div className="carousel-item">
                <img src={carrosule} className="d-block w-100" />
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
            <div className="marcas">
                <img src={marca1} alt="Marca1"/>
            </div>

            <div className="marcas">
                <img src={marca1} alt="Marca2"/>
            </div>

            <div className="marcas">
                <img src={marca1} alt="Marca3"/>
            </div>

            <div className="marcas">
                <img src={marca1} alt="Marca4"/>
            </div>
        </div>
        <h1 className="encabezadoHome"> Algunos de nuestros productos </h1>

        <Productos urlBack={urlBack} />

    </>
    );








}