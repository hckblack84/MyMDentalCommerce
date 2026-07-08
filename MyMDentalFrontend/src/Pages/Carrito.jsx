import React, { useEffect, useState } from 'react'
//import { useCarrito } from '../Components/CartContext'
import { useCarrito } from '../Context/CartContext';
import '../Styles/Carrito.css'
import { Wallet } from '@mercadopago/sdk-react';

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

    
    const [preferenceId, setPreferenceId] = useState('');
    const [hasId, setHasId] = useState(false);
    const [isLoadingPayment, setIsLoadingPayment] = useState(false);
    

    
const createOrderAndFetchPreferenceId = async () => {
    if (cart.length === 0) {
        //setError(true); // Hay un error
        //setErrorBody("Tu carrito está vacío."); // Mensaje del error
        return;
    }

    setIsLoadingPayment(true);
    //setError(false);    
    //setErrorBody('');     

    try {
        const carritoBack = cart.map((producto) => ({
            idProduct: producto.idProduct,
            quantityReserved: producto.quantity,
        }));

        const response = await fetch('/MyMDentalCommerce/pay/createOrder', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify(carritoBack)
        });

        if (!response.ok) {
            // Si el backend envía un mensaje de error, podríamos intentar leerlo
            const errorText = await response.text();
            throw new Error(errorText || `Error al crear la orden: ${response.status}`);
        }

        const id = (await response.text()).trim();
        console.log('Preference ID recibido:', id);

        if (id && id.length > 10) {
            setPreferenceId(id);
        } else {
            throw new Error('El ID de preferencia recibido no es válido');
        }
    } catch (err) {
        console.error('Error en el proceso de pago:', err);
        //setError(true); // Hay un error
        //setErrorBody(err.message || 'No se pudo iniciar el proceso de pago. Inténtalo de nuevo.'); // Mensaje del error
        setPreferenceId(null);
    } finally {
        setIsLoadingPayment(false);
    }
};


    const handleCheckout = async () => {

    setIsLoading(true);
    setError(null);

    try {
        const petitions = cart.map(item => ({
            idProduct: item.id,
            quantityReserved: item.quantity,
        }));

        const backendUrl = '/MyMDentalCommerce/pay/createOrder';
        
        
        const response = await fetch(backendUrl, {
            method: 'POST',
            body: JSON.stringify(petitions),
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        const checkoutUrl = response.data;
        if (checkoutUrl) {
            window.location.href = checkoutUrl;
        } else {
            throw new Error("No se recibió la URL de pago.");
        }

    } catch (err) {
        setIsLoading(false);
    }
};

     return (
        <>
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
        
{preferenceId && !error && (
    <Wallet
        initialization={{ preferenceId: preferenceId }}
        customization={{ texts:{ valueProp: 'smart_option'}}}
    />
)}

{error && <p style={{ color: 'red' }}>{errorBody}</p>}

        <button onClick={() => createOrderAndFetchPreferenceId()}>get id</button>
        <button onClick={() => console.log('ID actual:', preferenceId)}>log id</button>
        </>
    )
}