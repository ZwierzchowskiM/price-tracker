import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import AddProductForm from './components/AddProductForm';
import ProductList from './components/ProductList';
import ProductDetails from './components/ProductDetails';
import WelcomePage from './components/WelcomePage'; // Import nowej strony startowej
import theme from './theme/theme'; 
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
                <Routes>
                    {/* Strona startowa */}
                    <Route path="/" element={<WelcomePage />} />

                    {/* Główna aplikacja */}
                    <Route
                        path="/main"
                        element={
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

                                {/* Komponenty aplikacji */}
                                <AddProductForm onProductAdded={handleProductAdded} userId={userId} />
                                <ProductList userId={userId} key={productsUpdated} />
                            </div>
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