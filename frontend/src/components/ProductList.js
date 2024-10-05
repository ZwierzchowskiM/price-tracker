import React, { useEffect, useState } from 'react';

const ProductList = () => {
    const [products, setProducts] = useState([]); // Zmienna stanu do przechowywania produktów

    // Pobieranie danych z backendu po za³adowaniu komponentu
    useEffect(() => {
        fetch('http://localhost:8080/api/products/') // Twój backend dzia³a na porcie 8080
            .then(response => response.json())
            .then(data => setProducts(data)) // Zapisanie danych do stanu
            .catch(error => console.error('B³¹d podczas pobierania produktów:', error));
    }, []); // Pusty array oznacza, ¿e useEffect wykona siê raz po zamontowaniu komponentu

    return (
        <div>
            <h2>Lista produktów</h2>
            <ul>
                {products.map(product => (
                    <li key={product.id}>
                        {product.name} - {product.url}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default ProductList;