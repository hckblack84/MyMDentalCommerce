import React from 'react'
//import { useCarrito } from '../Components/CartContext'
import { useCarrito } from '../Context/CartContext';
import '../Styles/Carrito.css'

import Loader from '../Components/Loader';

export const Carrito = () => {
    const {
        cart,
        loadingPetition,
        error,
        errorBody,
        addToCart,
        reduceQuantityFromCart,
        deleteFromCart,
        deleteCart,
        getAllQuantityFromCart,
        getTotalPriceFromCart,
        getTotalPriceFromProduct,
        saveCartInLocalStorage,
        getCartFromLocalStorage,
        confirmPurchase,
    } = useCarrito();

     return (
        <div className="cart-container">
            {loadingPetition && <Loader entity='Peticion de carrito'/>}
            {error && <p style={{color: 'red'}}>{errorBody?.message || 'Error al procesar la compra'}</p>}
            <h2>Carrito de Compras</h2>

            {cart.length === 0 ? (
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
                        {cart.map((producto) => {
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
                                            onClick={() => reduceQuantityFromCart(producto)}
                                        >
                                            -
                                        </button>

                                        <span>{producto.quantity}</span>

                                        <button 
                                            className="quantity-btn"
                                            onClick={() => addToCart(producto)}
                                        >
                                            +
                                        </button>
                                    </div>

                                    <p>${getTotalPriceFromProduct(producto)}</p>

                                    <button 
                                        className='remove-btn'
                                        onClick={() => deleteFromCart(producto)}
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
                    Total del carrito: <strong>${getTotalPriceFromCart()}</strong>
                </p>
                <button className='purchase-btn' onClick={() => confirmPurchase()} disabled={cart.length === 0}>
                    Finalizar Compra
                </button>
            </div>
        </div>
    )
}