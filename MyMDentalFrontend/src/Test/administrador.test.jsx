import { render, screen, fireEvent, act } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Administrador from "../Pages/Administrador";

const mockDepartments = [
  { nameDepartment: "Odontologia general" },
  { nameDepartment: "Blanqueamiento" },
];

const mockUsersList = [
  { nameUser: "Juan", surnameUser: "Perez", emailUser: "juan@mail.com", cellphoneUser: "123456789", role: "CLIENT" },
];

beforeEach(() => {
  jest.clearAllMocks();

  global.fetch = jest.fn((url) => {
    if (url.includes("/departments")) {
      return Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockDepartments),
      });
    }
    if (url.includes("/getProducts")) {
      return Promise.resolve({
        ok: true,
        json: () => Promise.resolve([]),
      });
    }
    if (url.includes("/users")) {
      return Promise.resolve({
        ok: true,
        json: () => Promise.resolve(mockUsersList),
      });
    }
    if (url.includes("/deleteProduct")) {
      return Promise.resolve({ ok: true, text: () => Promise.resolve("") });
    }
    if (url.includes("/delete")) {
      return Promise.resolve({ ok: true, text: () => Promise.resolve("") });
    }
    return Promise.resolve({ ok: true, json: () => Promise.resolve({}) });
  });
});

afterEach(() => {
  delete global.fetch;
});

describe("Administrador", () => {

  test("renderiza el panel de administracion y los botones de seccion", async () => {
    render(
      <MemoryRouter>
        <Administrador />
      </MemoryRouter>
    );

    expect(screen.getByText("Panel de administración")).toBeInTheDocument();
    expect(screen.getByText("Gestión")).toBeInTheDocument();

    expect(screen.getByText("Productos")).toBeInTheDocument();
    expect(screen.getByText("Usuarios")).toBeInTheDocument();
  });

  test("renderiza los subtabs de productos por defecto", async () => {
    render(
      <MemoryRouter>
        <Administrador />
      </MemoryRouter>
    );

    expect(screen.getByText("Registrar")).toBeInTheDocument();
    expect(screen.getByText("Actualizar")).toBeInTheDocument();
    expect(screen.getByText("Eliminar")).toBeInTheDocument();
  });

  test("muestra el formulario de registrar producto por defecto", async () => {
    render(
      <MemoryRouter>
        <Administrador />
      </MemoryRouter>
    );

    expect(screen.getByPlaceholderText("ej. PROD-001")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Nombre")).toBeInTheDocument();
    expect(screen.getByText("Registrar producto")).toBeInTheDocument();
  });

  test("cambia al tab de Actualizar producto y muestra sus campos", async () => {
    render(
      <MemoryRouter>
        <Administrador />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText("Actualizar"));

    expect(screen.getByPlaceholderText("Nombre exacto")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("ej. PROD-002")).toBeInTheDocument();
    expect(screen.getByText("Actualizar producto")).toBeInTheDocument();
  });

  test("cambia al tab de Eliminar producto y muestra la zona de peligro", async () => {
    render(
      <MemoryRouter>
        <Administrador />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText("Eliminar"));

    expect(screen.getByText("Zona de peligro")).toBeInTheDocument();
    expect(screen.getByText("Eliminar producto")).toBeInTheDocument();
  });

  test("cambia a seccion Usuarios y renderiza sus subtabs", async () => {
    render(
      <MemoryRouter>
        <Administrador />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText("Usuarios"));

    expect(screen.getByText("Registrar")).toBeInTheDocument();
    expect(screen.getByText("Actualizar")).toBeInTheDocument();
    expect(screen.getByText("Eliminar")).toBeInTheDocument();
    expect(screen.getByText("Listar")).toBeInTheDocument();
  });

  test("navega entre tabs de Usuarios correctamente", async () => {
    render(
      <MemoryRouter>
        <Administrador />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText("Usuarios"));

    fireEvent.click(screen.getByText("Eliminar"));
    expect(screen.getByText("Zona de peligro")).toBeInTheDocument();
    expect(screen.getByText(/Ingresa el email exacto del usuario a eliminar/)).toBeInTheDocument();

    fireEvent.click(screen.getByText("Listar"));
    expect(screen.getByText("Recargar")).toBeInTheDocument();
  });

  test("renderiza los campos del formulario de registrar usuario", async () => {
    render(
      <MemoryRouter>
        <Administrador />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText("Usuarios"));

    expect(screen.getByPlaceholderText("Nombre")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Apellido")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("correo@ejemplo.com")).toBeInTheDocument();
  });

  
});
