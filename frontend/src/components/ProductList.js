import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const ProductList = ({ userId }) => {
    const [products, setProducts] = useState([]);
    const navigate = useNavigate();

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

    const handleDelete = async (productId) => {
        const response = await fetch(`http://localhost:8080/api/products/${productId}?userId=${userId}`, {
            method: 'DELETE',
        });

        if (response.ok) {
            // Usuń produkt z listy po jego usunięciu z serwera
            setProducts(products.filter(product => product.id !== productId));
        } else {
            console.error('Błąd podczas usuwania produktu');
        }
    };

    const handleCheckPrice = async (productId) => {
        const response = await fetch(`http://localhost:8080/api/products/${productId}/check-price`, {
            method: 'GET',
        });

        if (response.ok) {
            console.log(`Cena dla produktu o ID ${productId} została zaktualizowana.`);
            // Opcjonalnie: możesz odświeżyć listę produktów, aby pokazać nową cenę
            fetchProducts(); // Odświeżenie listy po sprawdzeniu ceny
        } else {
            console.error('Błąd podczas sprawdzania ceny produktu');
        }
    };

    const handleDetailsClick = (productId) => {
        navigate(`/products/${productId}`); // Przenosi na stronę szczegółów produktu
    };

    return (
        <div>
            <h2>Lista produktów</h2>
            <table>
                <thead>
                    <tr>
                        <th>Nazwa produktu</th>
                        <th>Link do produktu</th>
                        <th>Cena</th>
                        <th>Akcje</th>
                    </tr>
                </thead>
                <tbody>
                    {products.map(product => (
                        <tr key={product.id}>
                            <td>{product.name}</td>
                            <td>
                                <a href={product.url} target="_blank" rel="noopener noreferrer">
                                    {product.url}
                                </a>
                            </td>
                            <td>{product.lastPrice} PLN</td>
                            <td>
                                <button onClick={() => handleCheckPrice(product.id)}>Sprawdź cenę</button>
                            </td>
                            <td>
                                <button onClick={() => handleDetailsClick(product.id)}>Szczegóły</button>
                            </td>
                            <td>
                                <button onClick={() => handleDelete(product.id)}>Usuń</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default ProductList;
