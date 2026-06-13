import { render, screen } from "@testing-library/react";
import { MemoryRouter, Routes, Route } from "react-router-dom";
import Categoria from "../Pages/Categoria";
import Productos from "../Components/Productos";

jest.mock("../Components/Productos", () => jest.fn(() => <div data-testid="mock-productos">Productos Mock</div>));

beforeEach(() => {
  jest.clearAllMocks();
});

describe("Categoria (Departamento)", () => {

  test("renderiza el nombre del departamento desde la URL", () => {
    render(
      <MemoryRouter initialEntries={["/categoria/Odontologia%20general"]}>
        <Routes>
          <Route path="/categoria/:nameDepartment" element={<Categoria />} />
        </Routes>
      </MemoryRouter>
    );

    expect(screen.getByText("Odontologia general")).toBeInTheDocument();
  });

  test("renderiza el componente Productos con isFiltered y filter correctos", () => {
    render(
      <MemoryRouter initialEntries={["/categoria/Blanqueamiento"]}>
        <Routes>
          <Route path="/categoria/:nameDepartment" element={<Categoria />} />
        </Routes>
      </MemoryRouter>
    );

    expect(Productos).toHaveBeenCalledWith(
      expect.objectContaining({ isFiltered: true, filter: "Blanqueamiento" }),
      undefined
    );
  });

  test("pasa el filter con el nombre del departamento desde la URL", () => {
    render(
      <MemoryRouter initialEntries={["/categoria/Odontologia%20general"]}>
        <Routes>
          <Route path="/categoria/:nameDepartment" element={<Categoria />} />
        </Routes>
      </MemoryRouter>
    );

    expect(Productos).toHaveBeenCalledWith(
      expect.objectContaining({ isFiltered: true, filter: "Odontologia general" }),
      undefined
    );
  });

});
