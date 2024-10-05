import React, { useEffect, useState } from 'react';

const ProductList = ({ userId }) => {
    const [products, setProducts] = useState([]);

    const fetchProducts = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/products/user/${userId}`);
            if (!response.ok) {
                throw new Error(`Błąd HTTP: ${response.status}`);
            }
            const data = await response.json();
            setProducts(data);
        } catch (error) {
            console.error('Błąd podczas pobierania produktów:', error);
        }
    };

    useEffect(() => {
        fetchProducts(); // Pobierz produkty, gdy komponent zostanie zamontowany
    }, [userId]); // Odśwież, gdy zmieni się userId

    return (
        <div>
            <h2>Lista produktów</h2>
            <ul>
                {products.map(product => (
                    <li key={product.id}>
                        {product.name} -
                        <a href={product.url} target="_blank" rel="noopener noreferrer">
                            {product.url}
                        </a>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ProductList;
