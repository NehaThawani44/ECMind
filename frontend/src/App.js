import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import PdfManager from './components/PdfManager'; // Verify this import

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);

export default App;  // This is the default export
