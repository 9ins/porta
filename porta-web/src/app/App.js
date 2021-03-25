import React from 'react';
import '../css/App.css';
import { BrowserRouter as Router, Switch, Route, Link } from "react-router-dom";

import TabFrame from './TabFrame';
import Login from "../login/login.component";
import SignUp from "../login/signup.component";

function App() {
  return (
  <Router>
    <Switch>
      <Route path="/" component={TabFrame} />
    </Switch>
  </Router>
  );
}

export default App;