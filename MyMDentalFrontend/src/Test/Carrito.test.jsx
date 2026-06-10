import { render, screen, fireEvent } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Productos from "../Components/Productos";

const mockAddToCart = jest.fn();

jest.mock("../Context/CartContext", () => ({
  useCarrito: () => ({
    addToCart: mockAddToCart
  })
}));

jest.mock("../Hooks/UseProducts", () => ({
  useProductsState: () => ({
    productsState: {
      products: [
        {
          idProduct: 2,
          codeProduct: "002",
          productName: "Cepillo dental",
          descriptionProduct: "Pasta para dientes sensibles",
          priceProduct: 5000,
          stockProduct: 20
        }
      ],
      loading: false,
      error: false,
      errorBody: null,
      searchProductsByPage: jest.fn()
    },
    pagesState: {
      maxPages: 1,
      currentPage: 1,
      loadingPages: false,
      errorPages: false,
      errorBodyPages: null
    }
  })
}));

describe("Añadir productos al carrito", () => {
  test("Añade el producto al carrito", () => {
    render(
      <MemoryRouter>
        <Productos />
      </MemoryRouter>
    );

    const boton = screen.getByRole("button", {
      name: /añadir al carro socio/i
    });

    fireEvent.click(boton);

    expect(mockAddToCart).toHaveBeenCalledWith({
      idProduct: 2,
      codeProduct: "002",
      productName: "Cepillo dental",
      descriptionProduct: "Pasta para dientes sensibles",
      priceProduct: 5000,
      stockProduct: 20
    });
  });
});