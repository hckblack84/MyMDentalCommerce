import React, { Children, createContext } from 'react'
import { useContext, useState } from 'react';

const CartContext = createContext();

export const CartProvider = ({children}) => {
    const [carrito, setCarrito] = useState([]);

    const agregarAlCarrito = (producto) => {
        setCarrito((carritoAnterior) => {
            const productoExistente = carritoAnterior.findIndex(
                (articulo) => articulo.idProduct === producto.idProduct
            );

            if (productoExistente >= 0) {
                const carritoActualizado = [...carritoAnterior];
                carritoActualizado[productoExistente].cantidad += producto.cantidad;
                return carritoActualizado;
            } else {
                return [...carritoAnterior, { ...producto, cantidad: 1 }];
            }
        });
    }
    const actualizarCantidad = (productoId, cantidad) => {
        setCarrito((carritoAnterior) =>
            carritoAnterior
                .map((producto) =>
                    producto.idProduct === productoId
                        ? { ...producto, cantidad: producto.cantidad + cantidad }
                        : producto
                )
        );
    };

    const eliminarProducto = (productoId) => {
        setCarrito((carritoAnterior)=>
        carritoAnterior.filter((producto)=> producto.idProduct !== productoId)
    )
    }


    return (
        <CartContext.Provider value={{ carrito, agregarAlCarrito, actualizarCantidad, eliminarProducto }}>
            {children}
        </CartContext.Provider>
   
  )
}
export const useCarrito = () => useContext(CartContext);