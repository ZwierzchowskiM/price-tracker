import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, useNavigate } from 'react-router-dom';
import AddProductForm from './components/AddProductForm';
import ProductList from './components/ProductList';
import ProductDetails from './components/ProductDetails';
import WelcomePage from './components/WelcomePage';
import LoginForm from './components/LoginForm'; // Import strony logowania
import theme from './theme/theme';
import { ThemeProvider } from '@mui/material/styles';
import TextField from '@mui/material/TextField';
import { Button } from '@mui/material';

function App() {
    const [userId, setUserId] = useState('');
    const [productsUpdated, setProductsUpdated] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false); // Stan logowania
    

    const handleProductAdded = () => {
        setProductsUpdated(!productsUpdated);
    };

    useEffect(() => {
        // Sprawdzanie, czy token JWT jest dostępny
        const token = localStorage.getItem('token');
        if (token) {
            setIsLoggedIn(true); // Ustaw użytkownika jako zalogowanego, jeśli token istnieje
        }
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('token'); // Usuń token
        setIsLoggedIn(false); // Ustaw stan użytkownika jako niezalogowany
    };

    // Funkcja, która będzie wywoływana po zalogowaniu
    const handleLoginSuccess = () => {
        setIsLoggedIn(true);  // Ustawienie stanu użytkownika na zalogowanego
    };

   

    return (
        <ThemeProvider theme={theme}>
            <Router>
                <Routes>
                    {/* Strona startowa */}
                    <Route path="/" element={<WelcomePage />} />

                    {/* Strona logowania */}
                    <Route path="/login" element={<LoginForm onLoginSuccess={handleLoginSuccess} />} /> {/* Przekazanie funkcji */}

                    {/* Główna aplikacja (dostępna tylko po zalogowaniu) */}
                    <Route
                        path="/main"
                        element={
                            isLoggedIn ? (
                                <div className="App">
                                    <h1>Moja aplikacja cenowa</h1>
                                    <TextField
                                        label="ID użytkownika"
                                        variant="outlined"
                                        value={userId}
                                        onChange={(e) => setUserId(e.target.value)}
                                        required
                                        sx={{ marginBottom: 2 }}
                                    />
                                    <Button variant="contained" color="secondary" onClick={handleLogout}>
                                        Wyloguj się
                                    </Button>
                                    {/* Komponenty aplikacji */}
                                    <AddProductForm onProductAdded={handleProductAdded} userId={userId} />
                                    <ProductList userId={userId} key={productsUpdated} />
                                </div>
                            ) : (
                                <LoginForm /> // Jeśli użytkownik nie jest zalogowany, wyświetl stronę logowania
                            )
                        }
                    />

                    {/* Strona szczegółów produktu */}
                    <Route path="/products/:productId" element={<ProductDetails />} />
                </Routes>
            </Router>
        </ThemeProvider>
    );
}

export default App;
