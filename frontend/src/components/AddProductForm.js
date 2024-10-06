import React, { useState } from 'react';
import { TextField, Button, Box } from '@mui/material';

const AddProductForm = ({ onProductAdded }) => {
    const [productUrl, setProductUrl] = useState('');
    const [userId, setUserId] = useState(''); // Pole ID użytkownika

    const handleSubmit = async (e) => {
        e.preventDefault();

        const response = await fetch(`http://localhost:8080/api/products?userId=${userId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ url: productUrl }),
        });

        if (response.ok) {
            console.log('Produkt dodany!');
            setProductUrl('');
            setUserId(''); // Zresetuj pole ID użytkownika po dodaniu produktu
            onProductAdded(); // Wywołaj funkcję odświeżającą listę produktów
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
                label="ID użytkownika"
                variant="outlined"
                value={userId}
                onChange={(e) => setUserId(e.target.value)}
                required
            />
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