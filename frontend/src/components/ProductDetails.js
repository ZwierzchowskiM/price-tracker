import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

const ProductDetails = () => {
    const { productId } = useParams();
    const [product, setProduct] = useState(null);
    const [priceHistory, setPriceHistory] = useState([]);

    const fetchProductDetails = async () => {
        const token = localStorage.getItem('token');  // Pobierz token JWT z localStorage

        if (!token) {
            console.error('Brak tokena. U¿ytkownik nie jest zalogowany.');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/products/${productId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,  // Do³¹cz token do nag³ówka
                    'Content-Type': 'application/json',  // Ustaw nag³ówki
                },
            });

            if (!response.ok) {
                throw new Error(`B³¹d HTTP: ${response.status}`);
            }

            const data = await response.json();
            setProduct(data);
        } catch (error) {
            console.error('B³¹d podczas pobierania szczegó³ów produktu:', error);
        }
    };

    const fetchPriceHistory = async () => {
        const token = localStorage.getItem('token');  // Pobierz token JWT z localStorage

        if (!token) {
            console.error('Brak tokena. U¿ytkownik nie jest zalogowany.');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/products/${productId}/price-history`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,  // Do³¹cz token do nag³ówka
                    'Content-Type': 'application/json',  // Ustaw nag³ówki
                },
            });

            if (!response.ok) {
                throw new Error(`B³¹d HTTP: ${response.status}`);
            }

            const data = await response.json();
            setPriceHistory(data);
        } catch (error) {
            console.error('B³¹d podczas pobierania historii cen:', error);
        }
    };

    useEffect(() => {
        fetchProductDetails();
        fetchPriceHistory();
    }, [productId]);

    if (!product) {
        return <div>£adowanie...</div>;
    }

    return (
        <div>
            <h1>{product.name}</h1>
            <p>Link do produktu: <a href={product.url} target="_blank" rel="noopener noreferrer">{product.url}</a></p>
            <p>Aktualna cena: {product.lastPrice} PLN</p>

            {/* Tabela z histori¹ cen */}
            <h3>Historia cen w tabeli</h3>
            <table>
                <thead>
                    <tr>
                        <th>Data sprawdzenia</th>
                        <th>Cena (PLN)</th>
                    </tr>
                </thead>
                <tbody>
                    {priceHistory
                        .sort((a, b) => new Date(b.dateChecked) - new Date(a.dateChecked))  // Sortowanie od najnowszych do najstarszych
                        .map((price) => (
                            <tr key={price.id}>
                                <td>{new Date(price.dateChecked).toLocaleDateString()}</td>
                                <td>{price.priceValue}</td>
                            </tr>
                        ))}
                </tbody>
            </table>

        </div>
    );
};

export default ProductDetails;