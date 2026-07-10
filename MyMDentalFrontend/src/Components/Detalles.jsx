import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import logoImagen from "../assets/Imagenes/Logomym.png";
import { useCarrito } from '../Context/CartContext';
import "../Styles/Detalles.css";

const API_PRODUCT_BY_ID = import.meta.env.VITE_API_5;

const Detalles = () => {
  const { idProduct } = useParams();
  const { addToCart } = useCarrito();

  const [producto, setProducto] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProducto = async () => {
      try {
        const response = await fetch(`${API_PRODUCT_BY_ID}/${idProduct}`, {
          method: "GET",
          credentials: "include",
        });

        if (!response.ok) {
          throw new Error("No fue posible cargar la información del producto.");
        }

        const data = await response.json();
        setProducto(data);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchProducto();
  }, [idProduct]);

  if (error) {
    return (
      <div className="product-details">
        <p className="text-danger">{error}</p>
      </div>
    );
  }

  if (!producto) {
    return (
      <div className="product-details">
        <h3>Cargando producto...</h3>
      </div>
    );
  }

  return (
    <div className="product-details">
      <div className="product-detail-card">

        <div className="row g-0">

          <div className="col-lg-5 product-detail-image">
            <img
              src={producto.urlProduct}
              alt={producto.productName}
            />
          </div>

          <div className="col-lg-7 product-detail-info">

            <span className="product-code">
              Código: {producto.idProduct}
            </span>

            <h1 className="product-title">
              {producto.productName}
            </h1>

            <p className="product-description">
              {producto.descriptionProduct}
            </p>

            <div className="product-attributes">

              <div>
                <b>Precio</b>
                <br />
                ${producto.priceProduct}
              </div>

              <div>
                <b>Stock</b>
                <br />
                {producto.stockProduct} unidades
              </div>

            </div>

            <div className="product-price">
              ${producto.priceProduct}
            </div>

            <div className="product-stock">
              Disponible: {producto.stockProduct} unidades
            </div>

            <button className="product-buy-btn"
              onClick={() => addToCart(producto)}
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="18"
                height="18"
                fill="currentColor"
                className="bi bi-basket-fill"
                viewBox="0 0 16 16"
              >
                <path d="M5.071 1.243a.5.5 0 0 1 .858.514L3.383 6h9.234L10.07 1.757a.5.5 0 1 1 .858-.514L13.783 6H15.5a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.5.5H15v5a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V9H.5a.5.5 0 0 1-.5-.5v-2A.5.5 0 0 1 .5 6h1.717z" />
              </svg>
              Agregar al carrito

            </button>

          </div>

        </div>

      </div>
    </div>
  );
};

export default Detalles;