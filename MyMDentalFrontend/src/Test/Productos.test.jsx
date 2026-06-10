import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Productos from "../Components/Productos";
import {fireEvent} from "@testing-library/react";


const mockSearchProductsByPage = jest.fn();

jest.mock("../Context/CartContext", () => ({
  useCarrito: () => ({
    addToCart: jest.fn()
  })
}));

jest.mock("../Hooks/UseProducts", () => ({
  useProductsState: () => ({
    productsState: {
      products: [
        {
          idProduct: 1,
          codeProduct: "001",
          productName: "Pasta Dental",
          descriptionProduct: "Pasta para dientes sensibles",
          priceProduct: 5000,
          stockProduct: 20
        }
      ],
      loading: false,
      error: false,
      errorBody: null,
      searchProductsByPage: mockSearchProductsByPage
    },
    pagesState: {
      maxPages: 2,
      currentPage: 1,
      loadingPages: false,
      errorPages: false,
      errorBodyPages: null
    }
  })
}));

describe("Productos", () => {

  test("muestra el nombre del producto", () => {
    render(
      <MemoryRouter>
        <Productos />
      </MemoryRouter>
    );

    expect(
      screen.getByText("Pasta Dental")
    ).toBeInTheDocument();
  });

  test("muestra el precio del producto", () => {
    render(
      <MemoryRouter>
        <Productos />
      </MemoryRouter>
    );

    expect(
      screen.getByText("Precio: $5000")
    ).toBeInTheDocument();
  });

  test("muestra el stock del producto", () => {
    render(
      <MemoryRouter>
        <Productos />
      </MemoryRouter>
    );

    expect(
      screen.getByText("Stock: 20")
    ).toBeInTheDocument();
  });


    test("cambia de página al hacer click en página 2", () => {
      render(
        <MemoryRouter>
          <Productos />
        </MemoryRouter>
      );
      const botonPagina2 = screen.getByRole("button", {
        name: "2"
      });
      fireEvent.click(botonPagina2);

      expect(mockSearchProductsByPage).toHaveBeenCalledWith(2);
    });


    beforeEach(() => {
      jest.clearAllMocks();
    });
    
});
