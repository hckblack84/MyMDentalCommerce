import React, { Children, createContext } from 'react'
import { useContext, useState } from 'react';

const CartContext = createContext();

export const CartProvider = ({children}) => {
    const [carrito, setCarrito] = useState([]);

    const agregarAlCarrito = (producto) => {
    setCarrito((carritoAnterior) => {

        if (producto.stockProduct <= 0) {
            alert("Lo sentimos, este producto está agotado.");
            return carritoAnterior;
        }

        const productoExistente = carritoAnterior.findIndex(
            (articulo) => articulo.idProduct === producto.idProduct
        );

        if (productoExistente >= 0) {
            const carritoActualizado = [...carritoAnterior];
            carritoActualizado[productoExistente].cantidad += 1;
            return carritoActualizado;
        } else {
            return [...carritoAnterior, { ...producto, cantidad: 1 }];
        }
    });
};
        const actualizarCantidad = (productoId, cambio) => {
            setCarrito((carritoAnterior) =>
                carritoAnterior.map((producto) => {
                    if (producto.idProduct === productoId) {

                        const nuevaCantidad = producto.cantidad + cambio;

                        if (nuevaCantidad < 1) {
                            return producto;
                        }

                        if (nuevaCantidad > producto.stockProduct) {
                            alert("No hay más stock disponible");
                            return producto;
                        }

                        return { ...producto, cantidad: nuevaCantidad };
                    }

                    return producto;
                })
            );
        };

    const eliminarProducto = (productoId) => {
        setCarrito((carritoAnterior)=>
        carritoAnterior.filter((producto)=> producto.idProduct !== productoId)
    )
    }

    const vaciarCarrito = () => {
        setCarrito([]);
    }


    return (
        <CartContext.Provider value={{ carrito, agregarAlCarrito, actualizarCantidad, eliminarProducto, vaciarCarrito }}>
            {children}
        </CartContext.Provider>
   
  )
}
export const useCarrito = () => useContext(CartContext);