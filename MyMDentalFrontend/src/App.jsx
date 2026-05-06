import './App.css'
import Navegacion from './Components/navegacion'
import Footer from './Components/Footer'
import Home from './Pages/home'
import Crear_cuenta from './Pages/Crear_cuenta'
import Nosotros from './Pages/Nosotros'
import Administrador from './Pages/Administrador'
import Categoria from './Pages/Categoria'
import InicioSesion from './Pages/Inicio_sesion'
import Detalles from './Components/Detalles'
import Trabajador from './Pages/Trabajador'
import { CartProvider } from './Components/CartContext'
import { BrowserRouter, Routes, Route } from "react-router-dom";
import  {Carrito} from './Pages/Carrito'

function App() {
  return (
    <BrowserRouter>

      <CartProvider>
      <Navegacion />
      <Routes>
        <Route path="/"element={<Home />} />
        <Route path="/Nosotros" element={<Nosotros />} />
        <Route path="/Crear_cuenta" element={<Crear_cuenta />} />
        <Route path="/Administrador" element={<Administrador />} />
        <Route path="/categoria/:nameDepartment" element={<Categoria />} />
        <Route path="/inicio_sesion" element={<InicioSesion />} />
        <Route path="/producto/:idProduct" element={<Detalles />} />
        <Route path="Trabajador" element={<Trabajador />} />
        <Route path="/carrito" element={<Carrito />} />
      </Routes>
      <Footer />
      </CartProvider>
    </BrowserRouter>
  )
}

export default App