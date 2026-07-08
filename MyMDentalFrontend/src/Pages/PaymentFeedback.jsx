import React, { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Link } from 'react-router-dom';

const PaymentFeedback = () => {
    const [searchParams] = useSearchParams();
    const [message, setMessage] = useState('');
    const [details, setDetails] = useState({});

    useEffect(() => {
        const status = searchParams.get('status');
        const paymentId = searchParams.get('payment_id');
        const externalReference = searchParams.get('external_reference');

        setDetails({
            status,
            paymentId,
            orderId: externalReference
        });

        switch (status) {
            case 'approved':
                setMessage('¡Pago aprobado! Tu compra ha sido confirmada.');
                localStorage.removeItem('cart');
                break;
            case 'pending':
            case 'in_process':
                setMessage('Tu pago está pendiente. Te notificaremos cuando se apruebe.');
                break;
            case 'failure':
            case 'rejected':
                setMessage('El pago fue rechazado. Por favor, intenta con otro medio de pago.');
                break;
            default:
                setMessage('Ha ocurrido un error inesperado.');
                break;
        }
    }, [searchParams]);

    return (
        <div style={{ textAlign: 'center', padding: '40px' }}>
            <h1>Resultado de tu Compra</h1>
            <h2>{message}</h2>
            {details.orderId && <p>ID de tu orden: {details.orderId}</p>}
            {details.paymentId && <p>ID del pago: {details.paymentId}</p>}
            <p>Recibirás una confirmación por correo electrónico con los detalles finales.</p>
            <Link to="/">Volver a la tienda</Link>
        </div>
    );
};

export default PaymentFeedback;