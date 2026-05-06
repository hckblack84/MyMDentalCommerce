import React from 'react'
import { useCarrito } from '../Components/CartContext'
import '../Styles/Carrito.css'

export const Carrito = () => {
    const { carrito, actualizarCantidad, eliminarProducto } = useCarrito();
    const handleAumentarCantidad = (productoId) => {
        actualizarCantidad(productoId, 1);
    }

    const handleDisminuir = (productoId)=>{
        const producto = carrito.find((cantidad) => cantidad.idProduct===productoId )
        if (producto.cantidad>1){
            actualizarCantidad(productoId,-1)
        }
    }
    const totalCarrito = carrito.reduce(
        (acc, producto) => acc + producto.priceProduct * producto.cantidad,
        0
    );


    const handlePurchase = () => {
        const isConfirmed = window.confirm("¿Deseas finalizar tu compra? ");
        if (!isConfirmed) {
            return;
        }

        fetch('http://localhost:8080/MyMDentalCommerce/Orders/saveNewReserved', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: "include",
            body: JSON.stringify(carrito)
        })
        .then((response) => response.json())
        .then((data) => {
            console.log(data);
        })
        .catch((error) => {
            console.error('Error:', error);
        });

    }

     return (
        <div className="cart-container">
            <h2>Carrito de Compras</h2>

            {carrito.length === 0 ? (
                <p>Tu carrito está vacío</p>
            ) : (
                <>
                    <div className="cart-header">
                        <p>Producto</p>
                        <p>Precio</p>
                        <p>Cantidad</p>
                        <p>Total</p>
                        <p>Acción</p>
                    </div>

                    <ul className="cart-items">
                        {carrito.map((producto) => {
                            const totalprecio = producto.priceProduct * producto.cantidad;

                            return (
                                <li className="cart-item" key={producto.idProduct}>
                                    <div className="product-info">
                                        <img 
                                            src={producto.imageProduct} 
                                            className="product-image" 
                                            alt={producto.productName}
                                        />
                                        <span>{producto.productName}</span>
                                    </div>

                                    <p>${producto.priceProduct}</p>

                                    <div className="quantity-controls">
                                        <button 
                                            className="quantity-btn"
                                            onClick={() => handleDisminuir(producto.idProduct)}
                                        >
                                            -
                                        </button>

                                        <span>{producto.cantidad}</span>

                                        <button 
                                            className="quantity-btn"
                                            onClick={() => handleAumentarCantidad(producto.idProduct)}
                                        >
                                            +
                                        </button>
                                    </div>

                                    <p>${totalprecio}</p>

                                    <button 
                                        className='remove-btn'
                                        onClick={() => eliminarProducto(producto.idProduct)}
                                    >
                                        🗑️
                                    </button>
                                </li>
                            );
                        })}
                    </ul>
                </>
            )}

            
            <div className="cart-summary">
                <h3>Resumen Pedido</h3>
                <p className='total'>
                    Total del carrito: <strong>${totalCarrito}</strong>
                </p>
                <button className='purchase-btn' onClick={handlePurchase} disabled={carrito.length === 0}>
                    Finalizar Compra
                </button>
            </div>
        </div>
    )
}