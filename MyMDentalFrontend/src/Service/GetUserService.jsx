const api_7 = import.meta.env.VITE_API_7;

export const getUsers = async () => {
    console.log(api_7)
  const response = await fetch(api_7)
  const data = await response.json().catch(() => null)

  if (!response.ok){
    const error = new Error("Error en la petición de usuarios")
    error.status = response.status
    error.body = data
    throw error
  }
  return data
}