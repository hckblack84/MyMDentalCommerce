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
import { initMercadoPago } from '@mercadopago/sdk-react';
import { useEffect } from 'react'
import PaymentFeedback from './Pages/PaymentFeedback';

const MP_INIT_KEY = '__mercadopago_initialized__';

function App() {
  useEffect(() => {
    if (typeof window === 'undefined') return;
    if (window[MP_INIT_KEY]) return;

    initMercadoPago('APP_USR-fcf538f0-3e17-4092-bde2-0b71f61bde59');
    window[MP_INIT_KEY] = true;
  }, []);
const role = localStorage.getItem("role")

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
        <Route path="/payment-feedback" element={<PaymentFeedback />} />
      </Routes>
      <Footer />
      </CartProvider>
    </BrowserRouter>
  )
}

export default App