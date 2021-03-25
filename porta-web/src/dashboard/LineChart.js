import React, { Component } from 'react';
import '../../node_modules/react-vis/dist/style.css';
import { VerticalGridLines, HorizontalGridLines, XAxis, YAxis, XYPlot, LineSeries, DiscreteColorLegend } from 'react-vis';
import randomColor from 'randomcolor'
import Grid from '@material-ui/core/Grid'

const requestOptions = {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json',
        'username': 'admin'
    },
};

class LineChart extends Component {

    constructor(props) {
        super(props);
        this.state = {
            type: props.type, 
            unit: props.unit, 
            dim: props.dim, 
            stroke: props.stroke, 
            xdomain: props.xdomain, 
            ydomain: props.ydomain, 
            element: props.element, 
            label: props.label, 
            refreshSec: props.refreshSec, 
            data: props.element.map((e, index) => [{ x: 0, y: 0 }])
        }
        console.log(this.state);
    }

    componentDidMount() {
        this.setIntervalId = setInterval(() => {
            this.startFetch();
        }, this.state.refreshSec*1000);
        this.startFetch();
    }

    componentWillUnmount() {
        clearInterval(this.setIntervalId);
    }

    startFetch() {
        fetch("/resources?type=" + this.props.type + "&unit=" + this.props.unit, requestOptions)
        .then(response => response.json())
        .then(json => {
            if (this.state.data[0].length > (this.state.xdomain[1] - this.state.xdomain[0])) {
                this.setState({
                    data: this.state.element.map((e, index) => this.state.data[index].slice(1, this.state.data[index].length).map((e, index) => ({ x: index, y: e.y })))
                })
            }
            this.setState({
                data: this.state.element.map((e, index) => [...this.state.data[index], { x: this.state.data[index][this.state.data[index].length - 1].x + 1, y: json[e] }]
                )}
            )
            if (this.state.type == "MEMORY") {
                this.setState({
                    ydomain: [0, parseInt(json.SystemTotal)]
                })
            }
        });
    }

    render() {
        return (
            <div className="LineChart" style={
                {
                    backgroundColor: "#fefefe",
                    border: '2px solid darkgreen',
                    width: this.state.dim[0],
                    height: (this.props.dim[1] + 50),
                }
            }>
                <DiscreteColorLegend orientation='horizontal' items={this.state.label.map((e, index) => (
                    {
                        title: e,
                        color: this.state.stroke[index]
                    }
                ))
                } />
                <XYPlot width={this.state.dim[0]} height={this.state.dim[1]} stroke="#5ecca8" xDomain={this.state.xdomain} yDomain={this.state.ydomain} >
                    <VerticalGridLines />
                    <HorizontalGridLines />
                    <XAxis tickFormat={(d) => ''}/>
                    <YAxis tickFormat={(d) => d + '' + ((this.state.unit === 'PCT') ? '%' : this.state.unit)} />                    
                    {
                        this.state.element.map((e, index) => (
                            <LineSeries key={index} data={this.state.data[index]} style={{stroke: this.state.stroke[index], strokeWidth: 1.4}}/>
                        ))
                    }
                </XYPlot>
            </div>
        );
    }
}

export default LineChart;