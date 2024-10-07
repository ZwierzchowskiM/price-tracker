import React, { useState } from 'react';
import { TextField, Button, Box } from '@mui/material';

const AddProductForm = ({ onProductAdded }) => {
    const [productUrl, setProductUrl] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        const token = localStorage.getItem('token'); // Pobieranie tokenu JWT z localStorage

        const response = await fetch(`http://localhost:8080/api/products/`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,  // Przekazujemy token JWT
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ url: productUrl }), // Wysyłamy tylko URL produktu
        });

        if (response.ok) {
            console.log('Produkt dodany!');
            setProductUrl('');
            onProductAdded();
        } else {
            console.log('Błąd podczas dodawania produktu');
        }
    };

    return (
        <Box
            component="form"
            onSubmit={handleSubmit}
            sx={{ display: 'flex', flexDirection: 'column', gap: 2, maxWidth: '400px', margin: '0 auto' }}
        >
            <TextField
                label="URL produktu"
                variant="outlined"
                value={productUrl}
                onChange={(e) => setProductUrl(e.target.value)}
                required
            />
            <Button type="submit" variant="contained" color="primary">
                Dodaj produkt
            </Button>
        </Box>
    );
};

export default AddProductForm;
