import React, { Component } from 'react';
import '../../node_modules/react-vis/dist/style.css';
import { VerticalGridLines, HorizontalGridLines, XAxis, YAxis, XYPlot, LineSeries, DiscreteColorLegend } from 'react-vis';
import randomColor from 'randomcolor'
import Grid from '@material-ui/core/Grid'
import FetchRequest from '../app/FetchRequest';

class LineChart extends Component {

    constructor(props) {
        super(props);
        this.state = {
            method: props.method,
            path: props.path,
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
        this.fetchRequest  = new FetchRequest().fetchGetLinechart;
    }

    componentDidMount() {
        this.fetchRequest(this);
        this.setIntervalId = setInterval(() => {
            this.fetchRequest(this);
        }, this.state.refreshSec*2000);
    }

    componentWillUnmount() {
        clearInterval(this.setIntervalId);
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