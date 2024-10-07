import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
    Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button
} from '@mui/material';


const ProductList = ({ userId }) => {
    const [products, setProducts] = useState([]);
    const navigate = useNavigate();

    const fetchProducts = async () => {
        const token = localStorage.getItem('token');  // Pobierz token JWT z localStorage
        console.log('Przesyłany token JWT:', token);

        if (!token) {
            console.error('Brak tokena. Użytkownik nie jest zalogowany.');
            return;  // Zatrzymaj, jeśli nie ma tokena
        }

        try {
            const response = await fetch('http://localhost:8080/api/products/user', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });


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
        fetchProducts(); 
    }, [userId]); 

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
        const token = localStorage.getItem('token');  // Pobierz token JWT z localStorage
        if (!token) {
            console.error('Brak tokena. Użytkownik nie jest zalogowany.');
            return;
        }

        const response = await fetch(`http://localhost:8080/api/products/${productId}/check-price`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,  // Dołącz token do nagłówka
                'Content-Type': 'application/json',
            },
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
        <TableContainer component={Paper}>
            <Table>
        
                <TableHead>
                    <TableRow>
                        <TableCell>Nazwa produktu</TableCell>
                        <TableCell>Link do produktu</TableCell>
                        <TableCell>Cena</TableCell>
                        <TableCell>Akcje</TableCell>
                    </TableRow>
                </TableHead>
                    <TableBody>
                    {products.map(product => (
                        <TableRow key={product.id}>
                            <TableCell>{product.name}</TableCell>
                            <TableCell>
                                <a href={product.url} target="_blank" rel="noopener noreferrer">
                                    {product.url}
                                </a>
                            </TableCell>
                            <TableCell>{product.lastPrice} PLN</TableCell>
                            <TableCell>
                                <button onClick={() => handleCheckPrice(product.id)}>Sprawdź cenę</button>
                            </TableCell>
                            <TableCell>
                                <button onClick={() => handleDetailsClick(product.id)}>Szczegóły</button>
                            </TableCell>
                            <TableCell>
                                <button onClick={() => handleDelete(product.id)}>Usuń</button>
                            </TableCell>
                        </TableRow>
                    ))}
                    </TableBody>
            </Table>
        </TableContainer>
    );
};

export default ProductList;
