import React, { Component } from 'react';

const requestGetOptions = {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json',
        'username': 'admin'
    },
}

const requestPostOptions = (body) => {
    return { 
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'username': 'admin'
        },
        body: JSON.stringify(body),
    }
}

class FetchRequest extends Component {

    fetchGetAsync = async (path) => {
        const response = await fetch(path, requestGetOptions);
        const json =  await response.json();
        return json;
    }

    fetchGetResource = (path) => {        
        return fetch(path, requestGetOptions)
        .then(response => response.json())
        .catch(error => console.log(error));
    }

    fetchGetBarchart = (path, callback) => {
        fetch(path, requestGetOptions)
        .then(response => response.json())
        .then(json => {            
            callback.setState ({
                data : callback.props.labels.map( (e, index) => [{ x: e, y: json[Object.keys(json).filter(v => v.startsWith(e))[0]]}] ) 
            });
        })
        .catch(err => console.log(err));    
    }

    fetchGetLinechart = (path, callback) => {
        fetch(path, requestGetOptions)
        .then(response => response.json()) 
        .then(json => {
            if (callback.state.data[0].length > (callback.state.xDomain[1] - callback.state.xDomain[0])) {
                callback.setState({
                    data: callback.props.elements.map((e, index) => callback.state.data[index].slice(1, callback.state.data[index].length).map((e, index) => ({ x: index, y: e.y })))
                })
            }
            callback.setState({
                    data: callback.props.elements.map((e, index) => [...callback.state.data[index], { x: callback.state.data[index][callback.state.data[index].length - 1].x + 1, y: json[e] }]
                )}
            )
            console.log(callback.props);
            if (callback.props.name == "memory") {
                callback.setState({
                    yDomain: [0, parseInt(json.systemTotal)]
                })
            }
        })
    }

    fetchPostRequest = (path, body) => {
        return fetch(path, requestPostOptions(body))
        .then(response => response.json())
        .catch(err => console.log(err))
    }
}

export default FetchRequest;