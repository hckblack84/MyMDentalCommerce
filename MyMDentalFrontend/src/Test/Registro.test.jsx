import { render, screen, waitFor  } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Crear_cuenta from "../Pages/Crear_cuenta";
import {fireEvent} from "@testing-library/react";


const InvalidNumbers=("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
const IvnvalidCharacters=["@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "+", "=", "{", "}", "[", "]", "|", "\\", ":", ";", "\"", "'", "<", ">", ",", ".", "?","/"]
const EmailContains=["@gmail.com", "@hotmail.com" ,"@outlook.com"]


describe("Registro de usuarios", () => {

    test("muestra error si el nombre tiene menos de 3 letras", () => {
        render(
            <MemoryRouter>
                <Crear_cuenta />
            </MemoryRouter>
        );

        const inputNombre = screen.getByPlaceholderText("Nombre");
        const botonSubmit = screen.getByRole("button", { name: /Registrarse/i });

        fireEvent.change(inputNombre, {
            target: { value: "Jo" }
        });

        fireEvent.click(botonSubmit);

        const mensajeError = screen.getByText(
            /El nombre debe tener al menos 3 caracteres/i
        );

        expect(mensajeError).toBeInTheDocument();
    });


    test("muestra error si el apellido tiene menos de 3 letras", () => {
        render(
            <MemoryRouter>
                <Crear_cuenta />
            </MemoryRouter>
        );
        const inputNombre = screen.getByPlaceholderText("Nombre");
        const inputApellido = screen.getByPlaceholderText("Apellido");
        const botonSubmit = screen.getByRole("button", { name: /Registrarse/i });

        fireEvent.change(inputNombre, {target: {value: "Juan"}});
        fireEvent.change(inputApellido, {target: {value: "Pe"}});

        fireEvent.click(botonSubmit);
        const mensajeError = screen.getByText("El apellido debe tener al menos 3 caracteres");
        expect(mensajeError).toBeInTheDocument();
    });


    test("muestra error si el correo no es valido", () => {
        render(<MemoryRouter> <Crear_cuenta /> </MemoryRouter>);

        const inputemail = screen.getByPlaceholderText("Email");
        const inputNombre = screen.getByPlaceholderText("Nombre");
        const inputApellido = screen.getByPlaceholderText("Apellido");
        const botonSubmit = screen.getByRole("button",{ name: /Registrarse/i });


        fireEvent.change(inputNombre, {target: { value: "Juan" }});
        fireEvent.change(inputApellido, {target: { value: "Perez" }});
        fireEvent.change(inputemail, {target: { value: "correo@novalido.com" }});

        fireEvent.click(botonSubmit);
        const mensajeError = screen.getByText("El email no es válido");
        expect(mensajeError).toBeInTheDocument();

    });


    test("muestra error si la contraseña tiene menos de 6 caracteres", () => {
        render(
            <MemoryRouter>
                <Crear_cuenta />
            </MemoryRouter>
        );
        const inputNombre = screen.getByPlaceholderText("Nombre");
        const inputApellido = screen.getByPlaceholderText("Apellido");
        const inputemail = screen.getByPlaceholderText("Email");
        const inputPassword = screen.getByPlaceholderText("Contraseña");
        const botonSubmit = screen.getByRole("button", { name: /Registrarse/i });

        fireEvent.change(inputNombre, {target: {value: "Juan"}});
        fireEvent.change(inputApellido, {target: {value: "Perez"}});
        fireEvent.change(inputemail, {target: {value: "pruebaJest@gmail.com"}});
        fireEvent.change(inputPassword, {target: {value: "12345"}});

        fireEvent.click(botonSubmit);
        const mensajeError = screen.getByText("La contraseña debe tener al menos 6 caracteres");
        expect(mensajeError).toBeInTheDocument();
    });


    test("muestra error si las contraseñas no coinciden", () => {
        render(
            <MemoryRouter>
                <Crear_cuenta />
            </MemoryRouter>
        );
        const inputNombre = screen.getByPlaceholderText("Nombre");
        const inputApellido = screen.getByPlaceholderText("Apellido");
        const inputemail = screen.getByPlaceholderText("Email");
        const inputPassword = screen.getByPlaceholderText("Contraseña");
        const inputPassword2 = screen.getByPlaceholderText("Repetir Contraseña");
        const botonSubmit = screen.getByRole("button", { name: /Registrarse/i });

        fireEvent.change(inputNombre, {target: {value: "Juan"}});
        fireEvent.change(inputApellido, {target: {value: "Perez"}});
        fireEvent.change(inputemail, {target: {value: "pruebaJest@gmail.com"}});
        fireEvent.change(inputPassword, {target: {value: "123456"}});
        fireEvent.change(inputPassword2, {target: {value: "654321"}});

        fireEvent.click(botonSubmit);
        const mensajeError = screen.getByText("Las contraseñas no coinciden");
        expect(mensajeError).toBeInTheDocument();

    });
    
    test("muestra error si el teléfono no tiene 9 dígitos", () => {
        render(
            <MemoryRouter>
                <Crear_cuenta />
            </MemoryRouter>
        );
        const inputNombre = screen.getByPlaceholderText("Nombre");
        const inputApellido = screen.getByPlaceholderText("Apellido");
        const inputemail = screen.getByPlaceholderText("Email");
        const inputPassword = screen.getByPlaceholderText("Contraseña");
        const inputPassword2 = screen.getByPlaceholderText("Repetir Contraseña");
        const inputTelefono = screen.getByPlaceholderText("Teléfono (9 dígitos)");
        const botonSubmit = screen.getByRole("button", { name: /Registrarse/i });


        fireEvent.change(inputNombre, {target: {value: "Juan"}});
        fireEvent.change(inputApellido, {target: {value: "Perez"}});
        fireEvent.change(inputemail, {target: {value: "pruebaJest@gmail.com"}});
        fireEvent.change(inputPassword, {target: {value: "123456"}});
        fireEvent.change(inputPassword2, {target: {value: "123456"}});
        fireEvent.change(inputTelefono, {target: {value: "1234567891"}});

        fireEvent.click(botonSubmit);
        const mensajeError = screen.getByText("El teléfono debe tener 9 dígitos");
        expect(mensajeError).toBeInTheDocument();
    });

    describe("Crear Cuenta", () => {

    beforeEach(() => {
        global.fetch = jest.fn(); 
        window.alert = jest.fn();});

  test("muestra alerta cuando el registro es exitoso", async () => {
    fetch.mockResolvedValue({ok: true});

    render(<Crear_cuenta />);
    fireEvent.change(screen.getByPlaceholderText("Nombre"), {target: { value: "Antonio" }});
    fireEvent.change(screen.getByPlaceholderText("Apellido"), {target: { value: "Vedia" }});
    fireEvent.change(screen.getByPlaceholderText("Email"), {target: { value: "antonio@gmail.com" }});
    fireEvent.change(screen.getByPlaceholderText("Contraseña"), {target: { value: "123456789" }});
    fireEvent.change(screen.getByPlaceholderText("Repetir Contraseña"), {target: { value: "123456789" }});
    fireEvent.change(screen.getByPlaceholderText("Teléfono (9 dígitos)"), {target: { value: "912345678" }});
    fireEvent.click(screen.getByRole("button", {name: /registrarse/i}));

    await waitFor(() => {
      expect(window.alert).toHaveBeenCalledWith(
        "Cuenta creada correctamente"
      );
    });
  });
});


});
