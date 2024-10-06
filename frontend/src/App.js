import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import AddProductForm from './components/AddProductForm';
import ProductList from './components/ProductList';
import ProductDetails from './components/ProductDetails'; // Import strony szczegółów produktu
import theme from './theme/theme'; // Import stworzony motyw
import { ThemeProvider } from '@mui/material/styles';
import TextField from '@mui/material/TextField';

function App() {
    const [userId, setUserId] = useState('');
    const [productsUpdated, setProductsUpdated] = useState(false);

    const handleProductAdded = () => {
        setProductsUpdated(!productsUpdated);
    };

    return (
        <ThemeProvider theme={theme}>
            <Router>
                <div className="App">
                    <h1>Moja aplikacja cenowa</h1>

                    {/* Użycie komponentu TextField z Material-UI */}
                    <TextField
                        label="ID użytkownika"
                        variant="outlined"
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                        required
                        sx={{ marginBottom: 2 }}
                    />

                    {/* Definicja tras */}
                    <Routes>
                        {/* Strona szczegółów produktu */}
                        <Route path="/products/:productId" element={<ProductDetails />} />

                        {/* Główna strona z listą produktów */}
                        <Route
                            path="/"
                            element={
                                <>
                                    <AddProductForm onProductAdded={handleProductAdded} userId={userId} />
                                    <ProductList userId={userId} key={productsUpdated} />
                                </>
                            }
                        />
                    </Routes>
                </div>
            </Router>
        </ThemeProvider>
    );
}

export default App;