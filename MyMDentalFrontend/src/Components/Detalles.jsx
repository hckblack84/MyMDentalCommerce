import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import logoImagen from '../assets/Imagenes/Logomym.png';


const Detalles = () => {
  const { idProduct } = useParams() 
  const [producto, setProducto] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    const fetchProducto = async () => {
      try {
        const response = await fetch(`http://localhost:8080/MyMDentalCommerce/products/getProduct/${idProduct}`) 
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
              Añadir al carro socio
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