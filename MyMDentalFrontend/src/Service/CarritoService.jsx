const api6 = import.meta.env.VITE_API_6;

export const saveNewReserved = async (cart) => {
  const response = await fetch(api6, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
    body: JSON.stringify(cart),
  });

  const data = await response.json();

  if (!response.ok) {
    const error = new Error('Error al guardar el carrito');
    error.status = response.status;
    error.body = data;
    throw error;
  }

  return data;
};