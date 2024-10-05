import React, { useState } from 'react';

const AddProductForm = ({ onProductAdded }) => {
    const [productUrl, setProductUrl] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        const response = await fetch('http://localhost:8080/api/products?userId=1', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ url: productUrl }),
        });

        if (response.ok) {
            console.log('Produkt dodany!');
            setProductUrl('');
            onProductAdded(); // Wywo�aj funkcj� od�wie�aj�c� list� produkt�w
        } else {
            console.log('B��d podczas dodawania produktu');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>
                URL produktu:
                <input
                    type="text"
                    value={productUrl}
                    onChange={(e) => setProductUrl(e.target.value)}
                    required
                />
            </label>
            <button type="submit">Dodaj produkt</button>
        </form>
    );
};

export default AddProductForm;