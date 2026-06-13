import { render, screen, fireEvent, act } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import LoginRegister from "../Pages/Login-Register";

const mockNavigate = jest.fn();

jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useNavigate: () => mockNavigate,
}));

beforeEach(() => {
  jest.clearAllMocks();
  jest.useFakeTimers();
  localStorage.clear();
});

afterEach(() => {
  jest.useRealTimers();
  delete global.fetch;
});

describe("LoginRegister", () => {

  test("renderiza el formulario de inicio de sesion por defecto", () => {
    render(
      <MemoryRouter>
        <LoginRegister />
      </MemoryRouter>
    );

    expect(screen.getByPlaceholderText("Correo electrónico")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Contraseña")).toBeInTheDocument();
    expect(screen.getAllByText("Iniciar Sesión").length).toBe(2);
  });

  test("cambia al formulario de registro al hacer clic en Crear Cuenta", () => {
    render(
      <MemoryRouter>
        <LoginRegister />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText("Crear Cuenta"));

    act(() => {
      jest.advanceTimersByTime(500);
    });

    expect(screen.getByPlaceholderText("Nombre")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Apellido")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Repetir contraseña")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Teléfono (9 dígitos)")).toBeInTheDocument();
    expect(screen.getByText("Registrarse")).toBeInTheDocument();
  });

  test("vuelve al login al hacer clic en Iniciar sesion desde registro", () => {
    render(
      <MemoryRouter>
        <LoginRegister />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText("Crear Cuenta"));
    act(() => { jest.advanceTimersByTime(500); });

    fireEvent.click(screen.getByText("Iniciar sesión"));
    act(() => { jest.advanceTimersByTime(500); });

    expect(screen.getByPlaceholderText("Correo electrónico")).toBeInTheDocument();
  });

  test("muestra error al enviar login con email vacio", () => {
    render(
      <MemoryRouter>
        <LoginRegister />
      </MemoryRouter>
    );

    fireEvent.click(screen.getAllByText("Iniciar Sesión")[1]);

    expect(screen.getByText("El correo es obligatorio")).toBeInTheDocument();
  });

  test("muestra error al enviar login sin contraseña", () => {
    render(
      <MemoryRouter>
        <LoginRegister />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText("Correo electrónico"), {
      target: { value: "test@gmail.com" },
    });
    fireEvent.click(screen.getAllByText("Iniciar Sesión")[1]);

    expect(screen.getByText("La contraseña es obligatoria")).toBeInTheDocument();
  });

  test("realiza login exitoso y redirige al inicio", async () => {
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      json: () => Promise.resolve({
        role: "CLIENT",
        useremail: "cliente@gmail.com",
        token: "token123",
      }),
    });

    render(
      <MemoryRouter>
        <LoginRegister />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText("Correo electrónico"), {
      target: { value: "cliente@gmail.com" },
    });
    fireEvent.change(screen.getByPlaceholderText("Contraseña"), {
      target: { value: "123456" },
    });
    fireEvent.click(screen.getAllByText("Iniciar Sesión")[1]);

    await act(async () => {
      await Promise.resolve();
    });

    expect(mockNavigate).toHaveBeenCalledWith("/");
    expect(localStorage.getItem("role")).toBe("CLIENT");
    expect(localStorage.getItem("useremail")).toBe("cliente@gmail.com");
  });

  test("muestra error en login con credenciales incorrectas", async () => {
    global.fetch = jest.fn().mockResolvedValue({
      ok: false,
    });

    render(
      <MemoryRouter>
        <LoginRegister />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByPlaceholderText("Correo electrónico"), {
      target: { value: "mal@mail.com" },
    });
    fireEvent.change(screen.getByPlaceholderText("Contraseña"), {
      target: { value: "123456" },
    });
    fireEvent.click(screen.getAllByText("Iniciar Sesión")[1]);

    await act(async () => {
      await Promise.resolve();
    });

    expect(screen.getByText("Credenciales incorrectas")).toBeInTheDocument();
  });

  test("muestra errores de validacion en el registro", () => {
    render(
      <MemoryRouter>
        <LoginRegister />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText("Crear Cuenta"));
    act(() => { jest.advanceTimersByTime(500); });

    fireEvent.click(screen.getByText("Registrarse"));
    expect(screen.getByText("El nombre debe tener al menos 3 caracteres")).toBeInTheDocument();
  });

  test("valida que el email del registro tenga dominio valido", () => {
    render(
      <MemoryRouter>
        <LoginRegister />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText("Crear Cuenta"));
    act(() => { jest.advanceTimersByTime(500); });

    fireEvent.change(screen.getByPlaceholderText("Nombre"), { target: { value: "Carlos" } });
    fireEvent.change(screen.getByPlaceholderText("Apellido"), { target: { value: "Lopez" } });
    fireEvent.change(screen.getByPlaceholderText("Correo electrónico"), { target: { value: "invalido" } });
    fireEvent.change(screen.getByPlaceholderText("Contraseña"), { target: { value: "123456" } });
    fireEvent.change(screen.getByPlaceholderText("Repetir contraseña"), { target: { value: "123456" } });
    fireEvent.change(screen.getByPlaceholderText("Teléfono (9 dígitos)"), { target: { value: "123456789" } });

    const form = screen.getByText("Registrarse").closest("form");
    fireEvent.submit(form);
    expect(screen.getByText("El email no es válido")).toBeInTheDocument();
  });

  test("registro exitoso cambia al modo login", async () => {
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
    });

    render(
      <MemoryRouter>
        <LoginRegister />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText("Crear Cuenta"));
    act(() => { jest.advanceTimersByTime(500); });

    fireEvent.change(screen.getByPlaceholderText("Nombre"), { target: { value: "Carlos" } });
    fireEvent.change(screen.getByPlaceholderText("Apellido"), { target: { value: "Lopez" } });
    fireEvent.change(screen.getByPlaceholderText("Correo electrónico"), { target: { value: "carlos@gmail.com" } });
    fireEvent.change(screen.getByPlaceholderText("Contraseña"), { target: { value: "123456" } });
    fireEvent.change(screen.getByPlaceholderText("Repetir contraseña"), { target: { value: "123456" } });
    fireEvent.change(screen.getByPlaceholderText("Teléfono (9 dígitos)"), { target: { value: "123456789" } });

    fireEvent.click(screen.getByText("Registrarse"));

    await act(async () => {
      await Promise.resolve();
    });

    act(() => { jest.advanceTimersByTime(500); });

    expect(screen.getByPlaceholderText("Correo electrónico")).toBeInTheDocument();
  });

});
