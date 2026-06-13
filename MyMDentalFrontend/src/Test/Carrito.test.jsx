import { render, screen, fireEvent } from "@testing-library/react";
import { Carrito } from "../Pages/Carrito";
import { MemoryRouter } from "react-router-dom";

const mockConfirmPurchase = jest.fn();

jest.mock("../Context/CartContext", () => ({
  useCarrito: () => ({
    cart: [
      {
        idProduct: 2,
        productName: "Cepillo dental",
        priceProduct: 5000,
        quantity: 1
      }
    ],
    loadingPetition: false,
    error: false,
    errorBody: null,
    addToCart: jest.fn(),
    reduceQuantityFromCart: jest.fn(),
    deleteFromCart: jest.fn(),
    deleteCart: jest.fn(),
    getAllQuantityFromCart: jest.fn(),
    getTotalPriceFromCart: jest.fn(() => 5000),
    getTotalPriceFromProduct: jest.fn(() => 5000),
    saveCartInLocalStorage: jest.fn(),
    getCartFromLocalStorage: jest.fn(),
    confirmPurchase: mockConfirmPurchase
  })
}));

test("ejecuta confirmPurchase al presionar Finalizar Compra", () => {
  render(
    <MemoryRouter>
      <Carrito />
    </MemoryRouter>
  );

  const boton = screen.getByRole("button", {
    name: /finalizar compra/i
  });

  fireEvent.click(boton);

  expect(mockConfirmPurchase).toHaveBeenCalledTimes(1);
});