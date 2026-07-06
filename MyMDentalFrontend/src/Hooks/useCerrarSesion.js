import { useNavigate } from "react-router-dom";
const API_SESSION = import.meta.env.VITE_API_SESSION;

export default function useCerrarSesion() {
    const navigate = useNavigate();
    const cerrarSesion = async () => {

        try {
            await fetch(`${API_SESSION}/logout`, {
                method: "POST",
                credentials: "include"
            });
        } catch (error) {
            console.error(error);
        }

        localStorage.removeItem("authToken");
        localStorage.removeItem("useremail");

        navigate("/login");
    };
    return { cerrarSesion };
}