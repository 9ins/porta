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

    fetchGet = async (path, type, unit) => {
        const response = await fetch(path + "?type=" + type + "&unit=" + unit, requestGetOptions);
        const json =  await response.json();
        return json;
    }

    fetchGetResource = async (path, type, unit, k) => {        
        const res = await fetch(path + "?type=" + type + "&unit=" + unit, requestGetOptions);
        const json = await res.json();
        return json[k];
    }

    fetchGetBarchart = (callback) => {
        fetch(callback.state.path+"?type=" + callback.state.type + "&unit=" + callback.state.unit, requestGetOptions)
        .then(response => response.json())
        .then(json => {            
            callback.setState ({
                data : callback.state.label.map( (e, index) => [{ x: e, y: Object.keys(json).filter(v => v.startsWith(e)).map(e => json[e])}] ) 
            });
        })
        .catch(err => console.log(err));    
    }

    fetchGetLinechart = (callback) => {
        fetch(callback.state.path+"?type=" + callback.state.type + "&unit=" + callback.state.unit, requestGetOptions)
        .then(response => response.json()) 
        .then(json => {
            if (callback.state.data[0].length > (callback.state.xdomain[1] - callback.state.xdomain[0])) {
                callback.setState({
                    data: callback.state.element.map((e, index) => callback.state.data[index].slice(1, callback.state.data[index].length).map((e, index) => ({ x: index, y: e.y })))
                })
            }
            callback.setState({
                data: callback.state.element.map((e, index) => [...callback.state.data[index], { x: callback.state.data[index][callback.state.data[index].length - 1].x + 1, y: json[e] }]
                )}
            )
            if (callback.state.type == "MEMORY") {
                callback.setState({
                    ydomain: [0, parseInt(json.systemTotal)]
                })
            }
        })
    }

    fetchPostRequest = (path, type, body) => {
        fetch(path + "?type=" + type, requestPostOptions(body))
        .then(response => console.log(response))
        .catch(err => console.log(err))
    }
}

export default FetchRequest;