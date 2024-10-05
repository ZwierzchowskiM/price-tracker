import React, { useState } from 'react';
import ProductList from './components/ProductList';
import AddProductForm from './components/AddProductForm';

function App() {
    const [userId, setUserId] = useState(''); // Zmienna przechowuj�ca ID u�ytkownika
    const [productsUpdated, setProductsUpdated] = useState(false); // Zmienna kontroluj�ca aktualizacj� listy produkt�w

    const handleProductAdded = () => {
        setProductsUpdated(!productsUpdated);
    };

    return (
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
            <AddProductForm onProductAdded={handleProductAdded} userId={userId} />
            <ProductList userId={userId} key={productsUpdated} />
        </div>
    );
}

export default App;