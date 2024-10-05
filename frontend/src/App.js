import React from 'react';
import './App.css';
import ProductList from './components/ProductList'; // Import komponentu

function App() {
  return (
    <div className="App">
      <h1>Moja aplikacja cenowa</h1>
      <ProductList /> {/* U¿ycie komponentu */}
    </div>
  );
}

export default App;