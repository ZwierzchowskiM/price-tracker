import React, { useEffect, useState } from 'react';

const ProductList = () => {
    const [products, setProducts] = useState([]); // Zmienna stanu do przechowywania produkt�w

    // Pobieranie danych z backendu po za�adowaniu komponentu
    useEffect(() => {
        fetch('http://localhost:8080/api/products/') // Tw�j backend dzia�a na porcie 8080
            .then(response => response.json())
            .then(data => setProducts(data)) // Zapisanie danych do stanu
            .catch(error => console.error('B��d podczas pobierania produkt�w:', error));
    }, []); // Pusty array oznacza, �e useEffect wykona si� raz po zamontowaniu komponentu

    return (
        <div>
            <h2>Lista produkt�w</h2>
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