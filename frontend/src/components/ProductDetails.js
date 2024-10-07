import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Box, Typography, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Link, AppBar, Toolbar, Button } from '@mui/material';

const ProductDetails = () => {
    const { productId } = useParams();
    const [product, setProduct] = useState(null);
    const [priceHistory, setPriceHistory] = useState([]);
    const navigate = useNavigate();

    const fetchProductDetails = async () => {
        const token = localStorage.getItem('token');  // Pobierz token JWT z localStorage

        if (!token) {
            console.error('Brak tokena. Użytkownik nie jest zalogowany.');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/products/${productId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,  // Dołącz token do nagłówka
                    'Content-Type': 'application/json',  // Ustaw nagłówki
                },
            });

            if (!response.ok) {
                throw new Error(`Błąd HTTP: ${response.status}`);
            }

            const data = await response.json();
            setProduct(data);
        } catch (error) {
            console.error('Błąd podczas pobierania szczegółów produktu:', error);
        }
    };

    const fetchPriceHistory = async () => {
        const token = localStorage.getItem('token');  // Pobierz token JWT z localStorage

        if (!token) {
            console.error('Brak tokena. Użytkownik nie jest zalogowany.');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/products/${productId}/price-history`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,  // Dołącz token do nagłówka
                    'Content-Type': 'application/json',  // Ustaw nagłówki
                },
            });

            if (!response.ok) {
                throw new Error(`Błąd HTTP: ${response.status}`);
            }

            const data = await response.json();
            setPriceHistory(data);
        } catch (error) {
            console.error('Błąd podczas pobierania historii cen:', error);
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/'); // Przekierowanie do strony głównej po wylogowaniu
    };

    useEffect(() => {
        fetchProductDetails();
        fetchPriceHistory();
    }, [productId]);

    if (!product) {
        return <Typography variant="h6" align="center">Ładowanie...</Typography>;
    }

    return (
        <>
            {/* Pasek nawigacyjny z przyciskiem "Wyloguj się" */}
            <AppBar position="static" sx={{ backgroundColor: '#FFC107' }}>
                <Toolbar>
                    <Typography variant="h6" sx={{ flexGrow: 1, color: '#000' }}>
                        Szczegóły Produktu
                    </Typography>
                    <Button variant="contained" color="secondary" onClick={handleLogout}>
                        Wyloguj się
                    </Button>
                </Toolbar>
            </AppBar>

            {/* Szczegóły produktu */}
            <Box sx={{ maxWidth: '800px', margin: '0 auto', padding: 3 }}>
                <Box sx={{ marginBottom: 3 }}>
                    <Typography variant="h5" component="div" gutterBottom>
                        {product.name}
                    </Typography>
                    <Typography variant="body1" gutterBottom>
                        Link do produktu:{" "}
                        <Link href={product.url} target="_blank" rel="noopener noreferrer">
                            {product.url}
                        </Link>
                    </Typography>
                    <Typography variant="h6" color="primary">
                        Aktualna cena: {product.lastPrice} PLN
                    </Typography>
                </Box>

                {/* Historia cen */}
                <Typography variant="h5" gutterBottom>
                    Historia cen
                </Typography>
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell><Typography variant="subtitle1" fontWeight="bold">Data sprawdzenia</Typography></TableCell>
                                <TableCell><Typography variant="subtitle1" fontWeight="bold">Cena (PLN)</Typography></TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {priceHistory
                                .sort((a, b) => new Date(b.dateChecked) - new Date(a.dateChecked))  // Sortowanie od najnowszych do najstarszych
                                .map((price) => (
                                    <TableRow key={price.id}>
                                        <TableCell>{new Date(price.dateChecked).toLocaleDateString()}</TableCell>
                                        <TableCell>{price.priceValue} PLN</TableCell>
                                    </TableRow>
                                ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </Box>
        </>
    );
};

export default ProductDetails;
