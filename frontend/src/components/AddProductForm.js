import React, { useState } from 'react';

const AddProductForm = ({ onProductAdded }) => {
    const [productUrl, setProductUrl] = useState('');
    const [userId, setUserId] = useState(''); // Nowe pole do ID użytkownika

    const handleSubmit = async (e) => {
        e.preventDefault();

        const response = await fetch(`http://localhost:8080/api/products?userId=${userId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ url: productUrl }),
        });

        if (response.ok) {
            console.log('Produkt dodany!');
            setProductUrl('');
            setUserId(''); // Zresetuj pole użytkownika po dodaniu produktu
            onProductAdded(); // Wywo�aj funkcję odswieżającą listę produktów
        } else {
            console.log('B��d podczas dodawania produktu');
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <label>
                ID użytkownika:
                <input
                    type="text"
                    value={userId}
                    onChange={(e) => setUserId(e.target.value)}
                    required
                />
            </label>
            <br />
            <label>
                URL produktu:
                <input
                    type="text"
                    value={productUrl}
                    onChange={(e) => setProductUrl(e.target.value)}
                    required
                />
            </label>
            <br />
            <button type="submit">Dodaj produkt</button>
        </form>
    );
};

export default AddProductForm;
