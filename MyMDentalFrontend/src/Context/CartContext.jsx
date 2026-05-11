import { createContext, useContext } from 'react';
import { useCart } from '../Hooks/UseCart';

const CartContext = createContext();

export const CartProvider = ({ children }) => {
  const cartState = useCart();

  return <CartContext.Provider value={cartState}>{children}</CartContext.Provider>;
};

export const useCarrito = () => useContext(CartContext);