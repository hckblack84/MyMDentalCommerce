import React, { useState } from 'react';
import '../Styles/Orders.css';
const API_GET_BY_ID = import.meta.env.VITE_API_7;
const API_CHECK = import.meta.env.VITE_API_8;
export default function Orders() {
  const [searchId, setSearchId] = useState('');
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const fetchOrder = async () => {
    if (!searchId.trim()) return;
    setError(null);
    setLoading(true);
    try {
      const res = await fetch(`${API_GET_BY_ID}/${searchId}`, {
        method: 'GET',
        credentials: 'include'
      });
      if (!res.ok) throw new Error('Orden no encontrada');
      const data = await res.json();
      setOrders(prev => {
        if (prev.some(o => o.idReserved === data.idReserved)) return prev;
        return [...prev, data];
      });
      setSearchId('');
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };
  const checkOrder = async (id) => {
    try {
      const res = await fetch(`${API_CHECK}/${id}`, {
        method: 'PUT',
        credentials: 'include'
      });
      if (!res.ok) throw new Error('Error al actualizar');
      setOrders(prev =>
        prev.map(o => (o.idReserved === id ? { ...o, activeReserved: false } : o))
      );
    } catch (err) {
      console.error(err);
    }
  };
  const removeOrder = (id) => {
    setOrders(prev => prev.filter(o => o.idReserved !== id));
  };
  const total = (order) => (order.quantityReserved * order.priceProduct).toFixed(2);
  return (
    <div className="orders-page">
      <h1 className="orders-title">Órdenes</h1>
      <div className="orders-search">
        <input
          className="orders-search-input"
          type="text"
          placeholder="Buscar por ID de orden"
          value={searchId}
          onChange={e => setSearchId(e.target.value)}
          onKeyDown={e => e.key === 'Enter' && fetchOrder()}
        />
        <button
          className="orders-search-btn"
          onClick={fetchOrder}
          disabled={loading}
        >
          {loading ? 'Buscando...' : 'Buscar'}
        </button>
      </div>
      {error && <p className="orders-error">{error}</p>}
      {orders.length > 0 && (
        <div className="orders-table-wrapper">
          <table className="orders-table">
            <thead>
              <tr>
                <th>ID Reserva</th>
                <th>Cliente</th>
                <th>Producto</th>
                <th>Cantidad</th>
                <th>Precio Unit.</th>
                <th>Total</th>
                <th>Estado</th>
                <th>Acción</th>
              </tr>
            </thead>
            <tbody>
              {orders.map(order => (
                <tr key={order.idReserved}>
                  <td>{order.idReserved}</td>
                  <td>{order.emailUserEntity}</td>
                  <td>{order.productName}</td>
                  <td>{order.quantityReserved}</td>
                  <td>${order.priceProduct}</td>
                  <td>${total(order)}</td>
                  <td>
                    <span className={`order-status ${order.activeReserved ? 'pending' : 'delivered'}`}>
                      {order.activeReserved ? 'Por entregar' : 'Entregado'}
                    </span>
                  </td>
                  <td className="orders-actions">
                    {order.activeReserved && (
                      <button
                        className="orders-btn-check"
                        onClick={() => checkOrder(order.idReserved)}
                      >
                        Marcar entregado
                      </button>
                    )}
                    <button
                      className="orders-btn-remove"
                      onClick={() => removeOrder(order.idReserved)}
                    >
                      Quitar
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
      {orders.length === 0 && !error && (
        <p className="orders-empty">
          Ingresa un ID de orden y presiona Buscar para visualizarla.
        </p>
      )}
    </div>
  );
}