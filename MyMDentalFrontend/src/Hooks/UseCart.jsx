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
      const nuevaCantidad = existingProduct.quantity + 1;
      if (nuevaCantidad > product.stockProduct) {
        alert("No hay más stock disponible");
        return;
      }
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
    if (cart.length === 0) {
    alert("El carrito está vacío");
    return;
  }

  const isConfirmed = window.confirm(
    "¿Deseas finalizar tu compra?"
  );

  if (!isConfirmed) return;

  setLoadingPetition(true);
  setError(false);
  setErrorBody(null);

  const carritoBack = cart.map((producto) => ({
    idProduct: producto.idProduct,
    quantityReserved: producto.quantity,
  }));

  try {

    // guardar reserva
    const reservedResponse = await saveNewReserved(carritoBack);

    // obtener código reserva
    const idReserved = reservedResponse[0].idReserved;

    // mensaje whatsapp
    let message = ' Nuevo pedido:%0A%0A';

    message += ` Código Reserva: ${idReserved}%0A%0A`;

    cart.forEach((producto) => {

      message += ` Producto: ${producto.productName}%0A`;
      message += ` Cantidad: ${producto.quantity}%0A`;
      message += ` Precio unitario: $${producto.priceProduct}%0A`;
      message += `Subtotal: $${producto.priceProduct * producto.quantity}%0A%0A`;

    });

    const total = getTotalPriceFromCart();

    message += ` TOTAL PEDIDO: $${total}`;

    // WhatsApp
    const phoneNumber = '56971536489';

    const whatsappUrl =
      `https://wa.me/${phoneNumber}?text=${message}`;

    window.open(whatsappUrl, '_blank');

    // limpiar carrito
    setCart([]);

    localStorage.removeItem('cart');

    alert("Pedido realizado correctamente");

  } catch (e) {

    setError(true);

    setErrorBody(
      e.body ?? {
        code: e.code ?? e.status,
        message: e.message,
      }
    );

  } finally {

    setLoadingPetition(false);

  }


    
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
