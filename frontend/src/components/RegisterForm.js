import React, { useState } from 'react';
import { TextField, Button, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const RegisterForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (password !== confirmPassword) {
            setError('Hasła nie są zgodne');
            return;
        }

        try {
            const response = await fetch('http://localhost:8080/api/users/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            });

            if (!response.ok) {
                throw new Error('B�Błądd podczas rejestracji');
            }

            console.log('Rejestracja zakończona sukcesem');
            navigate('/login'); 
        } catch (error) {
            setError('Błąd podczas rejestracji');
            console.log('Error:', error);
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <TextField
                label="Email"
                variant="outlined"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
                sx={{ marginBottom: 2, maxWidth: '400px', width: '100%' }}
            />
            <TextField
                label="Hasło"
                variant="outlined"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                sx={{ marginBottom: 2, maxWidth: '400px', width: '100%' }}
            />
            <TextField
                label="Potwierdź hasło"
                variant="outlined"
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
                sx={{ marginBottom: 2, maxWidth: '400px', width: '100%' }}
            />
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <Button
                type="submit"
                variant="contained"
                color="primary"
                sx={{ maxWidth: '400px', width: '100%' }}
            >
                Zarejestruj się
            </Button>
        </Box>
    );
};

export default RegisterForm;
