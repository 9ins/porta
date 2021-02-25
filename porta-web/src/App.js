import React from 'react';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";

import Login from "./login/login.component";
import SignUp from "./login/signup.component";

function App() {
  return (
  <Router>
    <Switch>
      <Route path="/" component={Login} />
    </Switch>
  </Router>
  );
}

export default App;