import React, { useState } from 'react';
import { TextField, Button, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const RegisterForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    // Wyrażenie regularne do sprawdzania poprawności adresu e-mail
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Sprawdzenie, czy e-mail ma poprawny format
        if (!emailRegex.test(email)) {
            setError('Podaj poprawny adres e-mail');
            return;
        }

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
                body: JSON.stringify({ email, password }),
            });

            if (!response.ok) {
                throw new Error('Błąd podczas rejestracji');
            }

            console.log('Rejestracja zakończona sukcesem');
            navigate('/login');
        } catch (error) {
            setError('Błąd podczas rejestracji');
            console.log('Error:', error);
        }
    };

    // Funkcja do obsługi przycisku "Wstecz"
    const handleBack = () => {
        navigate(-1); // Cofnięcie do poprzedniej strony
    };

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 4, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <TextField
                label="Email"
                variant="outlined"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                sx={{ marginBottom: 2, maxWidth: '400px', width: '100%' }}
                type="email"
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
            {/* Dodajemy przycisk Wstecz */}
            <Button
                variant="outlined"
                color="secondary"
                onClick={handleBack}
                sx={{ mt: 2, maxWidth: '400px', width: '100%' }}
            >
                Wstecz
            </Button>
        </Box>
    );
};

export default RegisterForm;
