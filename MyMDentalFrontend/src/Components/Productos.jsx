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
    <div className="row row-cols-1 row-cols-md-4 g-4 mx-auto">
      {products.map(producto => (
        <div className="col" key={producto.codeProduct}>
          <div className="card-tecnica">
            <div className="card-tecnica__image">
              <img
                src={logoImagen}
                alt={producto.productName}
                className="card-tecnica__img"
              />
            </div>
            <span className="card-tecnica__badge">Código: {producto.codeProduct}</span>
            <h3 className="card-tecnica__title">{producto.productName}</h3>
            <p className="card-tecnica__desc">{producto.descriptionProduct}</p>

            <div className="card-tecnica__grid">
              <div className="card-tecnica__grid-item">
                <b>Stock:</b> {producto.stockProduct}
              </div>
              <div className="card-tecnica__grid-item">
              </div>
            </div>

            <div className="card-tecnica__footer">
              <span className="card-tecnica__price">${producto.priceProduct}</span>
              <div className="card-tecnica__actions">
                <Link
                  to={`/producto/${producto.idProduct}`}
                  className="card-tecnica__detalles"
                >
                  Detalles
                </Link>
                <button
                  className="card-tecnica__comprar"
                  onClick={() => addToCart(producto)}
                >
                  Comprar
                </button>
              </div>
            </div>
          </div>
        </div>
      ))}
    </div>
      <div className="pagination-container">
        {pagesButtons.map((page) => (
          <button
            key={page}
            className={`pagination-button ${currentPage === page ? 'active' : ''}`}
            onClick={() => getDatesByProductsPage(page)}
          >
            {page}
          </button>
        ))}
      </div>
  

    </>
  )
}