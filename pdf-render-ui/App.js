import React from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import PdfList from './components/PdfList';
import PdfUploader from './components/PdfUploader';
import PdfManager from './components/PdfManager';

function App() {
  return (
    <Router>
      <div className="App">
        <h1>PDF Stamper</h1>
        <Switch>
          <Route exact path="/" component={PdfList} />
          <Route path="/upload" component={PdfUploader} />
          <Route path="/pdf/:id" component={PdfManager} />
        </Switch>
      </div>
    </Router>
  );
}

export default App;
