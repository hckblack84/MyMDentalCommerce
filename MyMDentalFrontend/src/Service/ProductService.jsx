const api5 = import.meta.env.VITE_API_5;

export const getProductById = async (id) => {
  const response = await fetch(api5 + "/" + id)
  const data = await response.json().catch(() => null)

  if (!response.ok){
    
    const error = new Error("Error en la petición del número máximo de páginas de productos")
    error.status = response.status
    error.body = data
    throw error
  }
  return data
}