import React from 'react';
import ReactDOM from 'react-dom';
import reportWebVitals from './app/reportWebVitals';
import './index.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/react-fontawesome';
import { BrowserRouter } from "react-router-dom";
import App from './app/App';
import Dashboard from './dashboard/Dashboard';

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();



