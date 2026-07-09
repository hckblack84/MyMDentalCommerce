import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import logoImagen from '../assets/Imagenes/Logomym.png';

const API_PRODUCT_BY_ID = import.meta.env.VITE_API_5;

const Detalles = () => {
  const { idProduct } = useParams() 
  const [producto, setProducto] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    const fetchProducto = async () => {
      try {
        const response = await fetch(`${API_PRODUCT_BY_ID}/${idProduct}`, {
          method: 'GET',
          credentials: 'include'
        });
        if (!response.ok){
          throw new Error("Error al cargar los detalles, tamo trabajando pa esto we ;(")
        }
        const data = await response.json(); 
        setProducto(data);
      } catch (error) {
        setError(error.message)
      }
    };

    fetchProducto()
  }, [idProduct])

  return (
    <div className='product-details'>
      {error && <p className="text-danger">{error}</p>}
      {
        producto ? (
            <div className="container" style={{ maxWidth: '5000px' }}>
      <div className="row g-0">
            <div className="col-md-4 d-flex align-items-center p-3">
          <img 
            src={logoImagen} 
            className="img-fluid rounded-start" 
            alt={producto.productName} 
          />
        </div>
        
        <div className="col-md-8">
          <div className="card-body">
            <h2 className="card-title fw-bold">{producto.productName}</h2>
            
            <p className="card-text text-muted mb-4">
              <small>Código: {producto.codeProduct}</small>
            </p>

            <p className="card-text fs-5">
              {producto.descriptionProduct}
            </p>
            
            <p className="card-text fw-bold text-primary fs-3 mt-4">
              ${producto.priceProduct}
            </p>
            
            <p className="card-text">
              <small className="text-body-secondary">
                Stock disponible: {producto.stockProduct} unidades
              </small>
            </p>

            <button className="btn btn-primary mt-3">
             <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-basket-fill" viewBox="0 0 16 16">
  <path d="M5.071 1.243a.5.5 0 0 1 .858.514L3.383 6h9.234L10.07 1.757a.5.5 0 1 1 .858-.514L13.783 6H15.5a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.5.5H15v5a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V9H.5a.5.5 0 0 1-.5-.5v-2A.5.5 0 0 1 .5 6h1.717zM3.5 10.5a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0zm2.5 0a.5.5 0 1 0-1 0v3a.5.5 0 0 0 1 0z"/>
</svg>
            </button>

          </div>
        </div>
      </div>
    </div>
        ) : (
          <p>Cargando producto...</p>
        )
      }
    </div>
  )
}

export default Detalles