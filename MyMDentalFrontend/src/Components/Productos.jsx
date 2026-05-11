import React, { useEffect, useState } from 'react'
import "../Styles/Productos.css";
import logoImagen from '../assets/Imagenes/Logomym.png';
import { Link } from 'react-router-dom';
//import { useCarrito } from '../Components/CartContext';
import { useCarrito } from '../Context/CartContext';
import { useProductsState } from '../Hooks/UseProducts';

import Loader from './Loader';


export default function Productos({ isFiltered = false, filter = "" }) {

  const { productsState, pagesState } = useProductsState(1, isFiltered, filter);
  const { products, loading, error, errorBody, searchProductsByPage } = productsState
  const { maxPages, currentPage, loadingPages, errorPages, errorBodyPages, getDatesByProductsPage } = pagesState

  const {addToCart} = useCarrito();

  if (loading || loadingPages) {
    return (
      <Loader entity='Productos'/>
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
        <button onClick={() => searchProductsByPage(1)}>Volver a cargar productos</button>
      </>
    )
  }

  if (errorPages){
    return(
      <>
      <h1>Error con los indices de páginas</h1>
      <p>code: {errorBodyPages?.code ?? "Unknown"}</p>
      <p>message: {errorBodyPages?.message ?? "Unknown"}</p>
      </>
    );
  }

  let pagesButtons = []
  for (let i = 1; i <= maxPages; i++){
    pagesButtons.push(i)
  }


  return (
    <>
    <div className="row row-cols-1 row-cols-md-4 g-4 container mx-auto">
      {products.map(producto => (
        <div className="col" key={producto.codeProduct}>
          <div className="card h-100 border border-black shadow-sm">
            <img 
              src={logoImagen}
              className="card-img-top" 
              alt={producto.productName} 
            />
            
            <div className="card-body" id={producto.idProduct}>
              <h5 className="card-title fw-bold">{producto.productName}</h5>
              <p className="card-text text-muted">
                {producto.descriptionProduct}
              </p>
              <p className="card-text fw-bold text-primary">
                Precio: ${producto.priceProduct}
              </p>
            </div>

            <div className="card-footer bg-transparent border-top-0 d-flex justify-content-between align-items-center">
              <small className="text-muted">Stock: {producto.stockProduct}</small>
              <Link to={`/producto/${producto.idProduct}`} className="btn btn-sm btn-outline-primary">
                Detalles
              </Link>           
            </div>

            <button
              className="btn btn-sm btn-primary w-100"
              onClick={() => addToCart(producto)}
            >
              Añadir al carro socio
            </button>
          </div>
        </div>
      ))}
    </div>

    <div className='container'>
        <div className='row'>
          {pagesButtons.map((indexButton) => {
            if (indexButton === currentPage){
              return(
                <button id={indexButton} className='col-1 align-center text-center' disabled>{indexButton}</button>
              );
            }else{
              return(
                <button id={indexButton} className='col-1 align-center text-center'
                onClick={() => searchProductsByPage(indexButton)}
                >{indexButton}</button>
              );
            }
          })}
        </div>

      </div>
    </>
  )
}