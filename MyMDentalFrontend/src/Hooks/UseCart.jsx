import { useState } from 'react';
import { saveNewReserved } from '../Service/CarritoService';

export const useCart = () => {
  const [cart, setCart] = useState([]);
  const [loadingPetition, setLoadingPetition] = useState(false);
  const [error, setError] = useState(false);
  const [errorBody, setErrorBody] = useState(null)

  const addToCart = (product) => {
    if (product.stockProduct <= 0) {
      alert("Lo sentimos, este producto está agotado.");
      return;
    }
    const existingProduct = cart.find((item) => item.idProduct === product.idProduct);
    if (existingProduct) {
      setCart(
        cart.map((item) =>
          item.idProduct === product.idProduct
            ? { ...item, quantity: item.quantity + 1 }
            : item
        )
      );
    } else {
      setCart([...cart, { ...product, quantity: 1 }]);
    }
  };

  const reduceQuantityFromCart = (product) => {
    const existingProduct = cart.find((item) => item.idProduct === product.idProduct);
    if (existingProduct && existingProduct.quantity > 1) {
      setCart(
        cart.map((item) =>
          item.idProduct === product.idProduct
            ? { ...item, quantity: item.quantity - 1 }
            : item
        )
      );
    }
  };

  const deleteFromCart = (product) => {
    setCart(cart.filter((item) => item.idProduct !== product.idProduct));
  };

  const deleteCart = () => {
    setCart([]);
  };

  const getAllQuantityFromCart = () => {
    return cart.reduce((accumulator, product) => accumulator + product.quantity, 0);
  };

  const getTotalPriceFromCart = () => {
    return cart.reduce((totalPrice, product) => totalPrice + product.priceProduct * product.quantity, 0);
  };

  const getTotalPriceFromProduct = (product) => {
    return product.priceProduct * product.quantity;
  };

  const saveCartInLocalStorage = () => {
    localStorage.setItem('cart', JSON.stringify(cart));
  };

  const getCartFromLocalStorage = () => {
    const cartFromStorage = localStorage.getItem('cart');
    console.log('Cart from localStorage:', cartFromStorage);
    if (cartFromStorage) {
      setCart(JSON.parse(cartFromStorage));
    }
  };

  const confirmPurchase = async () => {
    setLoadingPetition(true);
    setError(false);
    setErrorBody(null);

    const carritoBack = cart.map((producto) => ({
      idProduct: producto.idProduct,
      quantityReserved: producto.quantity,
    }));

    
    const response = saveNewReserved(carritoBack);
    response
    .then(() => alert("Pedido agendado exitosamente"))
    .catch((e) => {setError(true), 
        setErrorBody(e.body ?? {
          code: e.code ?? e.status,
          message: e.message,
        });
    })
    .finally(() => setLoadingPetition(false))
    return response;
  };

  return {
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
  };
};
