import React from 'react';
import { BrowserRouter as Router, Route, Routes, useNavigate } from 'react-router-dom';
import { AppBar, Toolbar, Button, Box, Typography } from '@mui/material';
import AddProductForm from './components/AddProductForm';
import ProductList from './components/ProductList';
import ProductDetails from './components/ProductDetails';
import WelcomePage from './components/WelcomePage';
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import AccountSettings from './components/AccountSettings';  // Import nowego komponentu
import theme from './theme/theme';
import { ThemeProvider } from '@mui/material/styles';

function App() {
    const [productsUpdated, setProductsUpdated] = React.useState(false);
    const [isLoggedIn, setIsLoggedIn] = React.useState(false);
    const navigate = useNavigate();

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
        navigate('/');
    };

    const handleAccountSettings = () => {
        navigate('/account');  // Przejście do strony "Moje konto"
    };

    return (
        <ThemeProvider theme={theme}>
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6" sx={{ flexGrow: 1 }}>
                        Moje produkty
                    </Typography>
                    {isLoggedIn && (
                        <>
                            <Button variant="contained" color="primary" onClick={handleAccountSettings} sx={{ marginRight: 2 }}>
                                Moje konto
                            </Button>
                            <Button variant="contained" color="secondary" onClick={handleLogout}>
                                Wyloguj się
                            </Button>
                        </>
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
                <Route path="/login" element={<LoginForm onLoginSuccess={() => { }} />} />
                <Route path="/register" element={<RegisterForm />} />
                <Route path="/products/:productId" element={<ProductDetails />} />
                <Route path="/account" element={<AccountSettings />} />  {/* Nowa trasa */}
            </Routes>
        </Router>
    );
}
