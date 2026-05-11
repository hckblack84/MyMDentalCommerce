const api1 = import.meta.env.VITE_API_1;
const api2 = import.meta.env.VITE_API_2;
const api3 = import.meta.env.VITE_API_3;
const api4 = import.meta.env.VITE_API_4;


export const getClientProductsByPage = async (pageIndex) => {
    console.log(api1 + "/" + pageIndex)
  const response = await fetch(api1 + "/" + pageIndex)
  const data = await response.json().catch(() => null)

  if (!response.ok) {
    const error = new Error("Error en la petición de productos")
    error.status = response.status
    error.body = data
    throw error
  }

  return data
}

export const getDatesByProducts = async () => {
    console.log(api2)
  const response = await fetch(api2)
  const data = await response.json().catch(() => null)

  if (!response.ok){
    const error = new Error("Error en la petición del número máximo de páginas de productos")
    error.status = response.status
    error.body = data
    throw error
  }
  return data
}

export const getClientProductsByPageFilter = async (departmentName, pageIndex) => {
  console.log(api3 + "/" + departmentName + "/" + pageIndex)
  const response = await fetch(api3 + "/" + departmentName + "/" + pageIndex)
  const data = await response.json().catch(() => null)

  if (!response.ok){
    const error = new Error("Error en la petición del número máximo de páginas de productos")
    error.status = response.status
    error.body = data
    throw error
  }
  return data
}  

export const getDatesByProductsFilter = async (departmentName) => {
    console.log(api4 + "/" + departmentName)
  const response = await fetch(api4 + "/" + departmentName)
  const data = await response.json().catch(() => null)

  if (!response.ok){
    const error = new Error("Error en la petición del número máximo de páginas de productos")
    error.status = response.status
    error.body = data
    throw error
  }
  return data
}

