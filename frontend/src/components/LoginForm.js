import React, { useState } from 'react';
import { TextField, Button, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';  // Dodaj import useNavigate

const LoginForm = ({ onLoginSuccess }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();  
    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            });

            if (!response.ok) {
                throw new Error('B��d podczas logowania');
            }

            const token = await response.text();
            console.log('otrzymany token JWT:', token);
            localStorage.setItem('token', token);  // Zapisz token w localStorage
            onLoginSuccess();  // Wywo�anie funkcji po sukcesie logowania
            navigate('/main');  // Przekierowanie po zalogowaniu
        } catch (error) {
            setError('Nieprawid�owe dane logowania');
            console.log('Error:', error);
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 4 }}>
            <TextField
                label="Nazwa użytkownika"
                variant="outlined"
                fullWidth
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
                sx={{ marginBottom: 2 }}
            />
            <TextField
                label="Hasło"
                variant="outlined"
                fullWidth
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                sx={{ marginBottom: 2 }}
            />
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <Button type="submit" variant="contained" color="primary" fullWidth>
                Zaloguj się
            </Button>
        </Box>
    );
};

export default LoginForm;
