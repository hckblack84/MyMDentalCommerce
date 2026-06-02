import './App.css'
import Navegacion from './Components/navegacion'
import Footer from './Components/Footer'
import Home from './Pages/home'
import Nosotros from './Pages/Nosotros'
import Administrador from './Pages/Administrador'
import Categoria from './Pages/Categoria'
import LoginRegister from './Pages/Login-Register'
import Detalles from './Components/Detalles'
import Trabajador from './Pages/Trabajador'
import EditarDatos from './Pages/EditarDatos'
import Perfil from './Pages/Perfil'
import Orders from './Pages/Orders'
import { CartProvider } from './Context/CartContext'
import { BrowserRouter, Routes, Route } from "react-router-dom";
import  {Carrito} from './Pages/Carrito'
import { Navigate } from 'react-router-dom';


function App() {

const role= localStorage.getItem("role")

  return (
    <BrowserRouter>
      <CartProvider>
      <Navegacion />
      <Routes>
        
          <Route
            path="/Orders"
            element={
              role === "ADMINISTRATOR" ||
              role === "WORKER"
                ? <Orders />
                : <Navigate to="/" />
            }
        />
        <Route path="/"element={<Home />} />
        <Route path="/Nosotros" element={<Nosotros />} />
        <Route path="/acceder" element={<LoginRegister />} />
        <Route path="/crear_cuenta" element={<Navigate to="/acceder" replace />} />
        <Route path="/inicio_sesion" element={<Navigate to="/acceder" replace />} />
        <Route 
            path="/Administrador"
            element={
              role === "ADMINISTRATOR"
                ? <Administrador />
                : <Navigate to="/" />
            }
          />

        <Route path="/categoria/:nameDepartment" element={<Categoria />} />

        <Route path="/producto/:idProduct" element={<Detalles />} />
        <Route path="/perfil" element={<Perfil />} />
        <Route path="/EditarDatos" element={<EditarDatos />} />
        
        <Route 
            path="/Trabajador"
            element={
              role === "WORKER" ||
              role === "ADMINISTRATOR"
                ? <Trabajador />
                : <Navigate to="/" />
            }
          />
        <Route path="/carrito" element={<Carrito />} />
      </Routes>
      <Footer />
      </CartProvider>
    </BrowserRouter>
  )
}

export default App