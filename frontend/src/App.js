import React, { useState } from 'react';
import ProductList from './components/ProductList';
import AddProductForm from './components/AddProductForm';

function App() {
    const [productsUpdated, setProductsUpdated] = useState(false);

    const handleProductAdded = () => {
        setProductsUpdated(!productsUpdated); // Zmieniaj stan po ka¿dym dodaniu produktu
    };

    return (
        <div className="App">
            <h1>Moja aplikacja cenowa</h1>
            <AddProductForm onProductAdded={handleProductAdded} />
            <ProductList key={productsUpdated} />
        </div>
    );
}

export default App;