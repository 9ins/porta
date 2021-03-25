import React, { Component } from 'react';

const requestOptions = {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json',
        'username': 'admin'
    },
};

class FetchRequest extends Component {

    constructor(props) {
        super(props);
        this.state = {
            path: props.path,
            method: props.method,
            type : props.type,
            unit : props.unit,
            element : props.element,
            data : props.element.map((e, index) => [{ x: e, y: 0 }])
        }
    }

    fetchGet() {
        fetch(this.state.path+"?type=" + this.props.type + "&unit=" + this.props.unit, requestOptions)
        .then(response => response.json())
        .then(json => {
            this.setState({
                data: this.state.element.map((e, index) => this.state.data[index] = [{x: this.state.label[index], y: json[e]}] ) 
            })
        });
        return this.state.data;
    }

    fetchPost() {
    }
}

export default FetchRequest;