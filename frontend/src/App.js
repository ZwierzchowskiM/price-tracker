import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import AddProductForm from './components/AddProductForm';
import ProductList from './components/ProductList';
import ProductDetails from './components/ProductDetails'; // Import strony szczegółów produktu

function App() {
    const [userId, setUserId] = useState('');
    const [productsUpdated, setProductsUpdated] = useState(false);

    const handleProductAdded = () => {
        setProductsUpdated(!productsUpdated);
    };

    return (
        <Router>
            <div className="App">
                <h1>Moja aplikacja cenowa</h1>
                <label>
                    ID użytkownika:
                    <input
                        type="text"
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                        required
                    />
                </label>

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
    );
}

export default App;