import React, { useEffect, useState } from 'react';

const ProductList = () => {
    const [products, setProducts] = useState([]);

    const fetchProducts = async () => {
        const response = await fetch('http://localhost:8080/api/products/');
        const data = await response.json();
        setProducts(data);
    };

    useEffect(() => {
        fetchProducts(); // Pobierz produkty, gdy komponent zostanie zamontowany
    }, []);

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