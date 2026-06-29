import { useNavigate } from "react-router-dom";

export default function useCerrarSesion() {
    const navigate = useNavigate();

    const cerrarSesion = () => {
        localStorage.removeItem("authToken");
        localStorage.removeItem("useremail");
        navigate("/login");
    };

    return { cerrarSesion };
}