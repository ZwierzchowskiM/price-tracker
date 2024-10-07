import React from 'react';
import { BrowserRouter as Router, Route, Routes, useNavigate } from 'react-router-dom'; // Dodano useNavigate
import { AppBar, Toolbar, Button, Box, Typography } from '@mui/material';
import AddProductForm from './components/AddProductForm';
import ProductList from './components/ProductList';
import ProductDetails from './components/ProductDetails';
import WelcomePage from './components/WelcomePage';
import LoginForm from './components/LoginForm';
import theme from './theme/theme';
import { ThemeProvider } from '@mui/material/styles';

function App() {
    const [productsUpdated, setProductsUpdated] = React.useState(false);
    const [isLoggedIn, setIsLoggedIn] = React.useState(false);
    const navigate = useNavigate();  // Teraz jest w kontekście Routera

    const handleProductAdded = () => {
        setProductsUpdated(!productsUpdated);
    };

    React.useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            setIsLoggedIn(true);
        }
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('token');
        setIsLoggedIn(false);
        navigate('/');  // Teraz nawigacja powinna działać poprawnie
    };

    return (
        <ThemeProvider theme={theme}>
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6" sx={{ flexGrow: 1 }}>
                        Moja aplikacja cenowa
                    </Typography>
                    {isLoggedIn && (
                        <Button variant="contained" color="secondary" onClick={handleLogout}>
                            Wyloguj się
                        </Button>
                    )}
                </Toolbar>
            </AppBar>

            <Box sx={{ padding: 3 }}>
                {isLoggedIn ? (
                    <>
                        <AddProductForm onProductAdded={handleProductAdded} />
                        <ProductList key={productsUpdated} />
                    </>
                ) : (
                    <LoginForm onLoginSuccess={() => setIsLoggedIn(true)} />
                )}
            </Box>
        </ThemeProvider>
    );
}

export default function Root() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<WelcomePage />} />
                <Route path="/main" element={<App />} />
                <Route path="/login" element={<LoginForm />} />
                <Route path="/products/:productId" element={<ProductDetails />} />
            </Routes>
        </Router>
    );
}
