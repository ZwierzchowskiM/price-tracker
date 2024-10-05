import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import PriceHistoryChart from './PriceHistoryChart'; // Import wykresu historii cen

const ProductDetails = () => {
    const { productId } = useParams();
    const [product, setProduct] = useState(null);
    const [priceHistory, setPriceHistory] = useState([]);

    const fetchProductDetails = async () => {
        const response = await fetch(`http://localhost:8080/api/products/${productId}`);
        const data = await response.json();
        setProduct(data);
    };

    const fetchPriceHistory = async () => {
        const response = await fetch(`http://localhost:8080/api/products/${productId}/price-history`);
        const data = await response.json();
        setPriceHistory(data);
    };

    useEffect(() => {
        fetchProductDetails();
        fetchPriceHistory();
    }, [productId]);

    if (!product) {
        return <div>�adowanie...</div>;
    }

    return (
        <div>
            <h1>{product.name}</h1>
            <p>Link do produktu: <a href={product.url} target="_blank" rel="noopener noreferrer">{product.url}</a></p>
            <p>Aktualna cena: {product.lastPrice} PLN</p>

            {/* Tabela z histori� cen */}
            <h3>Historia cen w tabeli</h3>
            <table>
                <thead>
                    <tr>
                        <th>Data sprawdzenia</th>
                        <th>Cena (PLN)</th>
                    </tr>
                </thead>
                <tbody>
                    {priceHistory.map((price) => (
                        <tr key={price.id}>
                            <td>{new Date(price.dateChecked).toLocaleDateString()}</td>
                            <td>{price.priceValue}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {/* Wy�wietlenie wykresu */}
            <h3>Historia cen</h3>
            <PriceHistoryChart productId={productId} /> {/* Wykres historii cen */}
        </div>
    );
};

export default ProductDetails;