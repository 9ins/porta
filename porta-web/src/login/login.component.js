import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { AlertList, Alert, AlertContainer } from "react-bs-notifier";
import 'whatwg-fetch';
import alertTransition from 'react-bs-notifier/lib/alert-transition';
import { BrowserRouter as Router, Switch, Route, Link, Redirect, BrowserRouter } from "react-router-dom";
import Dashboard from '../dashboard/Dashboard';

class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            status: 'false',
            message: '',
            isChecked: false,
            redirect: ''
        };
        this.handleChange = this.handleUserChange.bind(this);
        this.handleChange = this.handlePassChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.sendLoginRequest = this.sendLoginRequest.bind(this);
        this.onChangeCheckbox = this.onChangeCheckbox.bind(this);
    }

    componentDidMount() {
        if (localStorage.isChecked && localStorage.username !== "") {
            console.log(localStorage.isChecked);
            this.setState({
                isChecked: localStorage.isChecked,
                username: localStorage.username,
                password: localStorage.password
            })
        }
    }

    sendLoginRequest() {
        // Simple POST request with a JSON body using fetch
        //alert(this.state.username+'   '+this.state.password);
        const requestOptions = {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                username: this.state.username,
                password: this.state.password
            },
            /*
            body: JSON.stringify({ 
                title: 'React POST Request Example',
                username: this.state.username,
                password: this.state.password,
            })
            */
        };
        //alert(requestOptions.body);
        fetch('http://localhost:3232/login', requestOptions)
            .then(response => response.json())
            .then(data => {
                this.setState({
                    //username: this.state.username,
                    //password: this.state.password,
                    status: data.status,
                    message: data.message,
                    redirect: data.redirect
                })                
                console.log('isChecked: '+this.state.isChecked);
                alert('redirect: '+this.state.redirect);
                if(this.state.isChecked) {
                    localStorage.username = this.state.username;
                    localStorage.password = this.state.password;
                    localStorage.isChecked = this.state.isChecked;
                }
            })
    }    

    handleUserChange(event) {
        this.setState({ 
            username: event.target.value , 
            password: this.state.password
        });
        console.log(this.state.username+'   '+this.state.password);
    }
    
    handlePassChange(event) {
        this.setState({ 
            username: this.state.username,
            password: event.target.value 
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        this.sendLoginRequest();
    }    

    onChangeCheckbox = event => {
        console.log(event.target.checked);
        this.setState({
            isChecked: event.target.checked
        })
    }    

    render() {
        if(this.state.status == 'true') {
            return (
                <Router>
                <Switch>
                    {
                        console.log(this.state.redirect)
                    }
                    <Route path={this.state.redirect} component={Dashboard} />
                </Switch>
                </Router>                
            )
        }
        return (            
            <div className="App">
                <nav className="navbar navbar-expand-lg navbar-light fixed-top">
                    <div className="title-icon">
                        <img src="/img/porta2_80.png"/>
                    </div>
                    <div className="container">          
                        <Link className="navbar-brand" to={"/"}>Porta management console</Link>
                    </div>
                </nav>
                <div className="auth-wrapper">
                   <div className="auth-inner">
                    <form method="POST" onSubmit={this.handleSubmit}>
                        <h3>Sign In</h3>
                        <div className="form-group">
                            <label>Username</label>
                            <input type="text" value={this.state.username} className="form-control" placeholder="Enter username" onChange={e => this.handleUserChange(e)}/>
                        </div>
                        <div className="form-group">
                            <label>Password</label>
                            <input type="password" value={this.state.password} className="form-control" placeholder="Enter password" onChange={e => this.handlePassChange(e)}/>
                        </div>
                        <div className="alert alert-success">
                            <div className="custom-control custom-checkbox">
                                <input type="checkbox" className="custom-control-input" checked={this.state.isChecked} id="customCheck1" onChange={e => this.onChangeCheckbox(e)} />
                                <label className="custom-control-label" htmlFor="customCheck1">Remember me</label>
                            </div>
                            {
                                (this.state.status == 'false') ? <strong>{this.state.message}</strong> : null
                            }
                        </div>
                        <button type="submit" className="btn btn-primary btn-block">Submit</button>
                    </form>
                </div>
            </div>
        </div>
        );
    }
}

export default Login;