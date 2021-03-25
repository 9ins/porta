import React, { Component } from 'react';
import { VerticalGridLines, HorizontalGridLines, VerticalBarSeries, VerticalBarSeriesCanvas, XAxis, YAxis, XYPlot, LabelSeries, DiscreteColorLegend } from 'react-vis';
import randomColor from 'randomcolor'
import { BottomNavigation } from '@material-ui/core';
import FetchRequest from '../app/FetchRequest';

class BarChart extends Component {

    constructor(props) {
        super(props);
        this.state = {
            type: props.type,
            unit: props.unit,
            dist: props.dist,
            dim: props.dim,
            color: props.color,
            element: props.element,
            label: props.label,
            refreshSec: props.refreshSec,
            data: props.element.map((e, index) => [{ 
                x: props.label[index], 
                y: 0 
            }])
        }
    }

    componentDidMount() {
        const fetch = new FetchRequest('/resources', 'get', 'THREAD', 'CNT', this.state.element);
        this.state.data = fetch.fetchGet();
        this.setIntervalId = setInterval(() => {
        }, this.state.refreshSec*1000);
        
    }

    componentWillUnmount() {
        clearInterval(this.setIntervalId);
    }

    render() {
        const BarSeries = VerticalBarSeries;
        return (
                <div style={
                    {
                        border: '1px solid darkblue',
                        backgroundColor: "#fefefe",
                        width: this.state.dim[0] + 30,
                        height: (this.props.dim[1] + 50),
                    }
                }>
                <DiscreteColorLegend orientation='horizontal' items={this.state.element.map((e, index) => (
                    {
                        title: this.state.label[index],
                        color: this.state.color[index],
                    }
                ))} />
                <XYPlot xType="ordinal" width={this.state.dim[0]} height={this.state.dim[1]} xDistance={this.state.dist}>
                <XAxis />
                {
                    this.state.element.map((e, index) => (
                        <BarSeries animation key={index} data={this.state.data[index]} color={this.state.color[index]} />
                    ))
                }                
                </XYPlot>
                </div>
        );
    }
}
  
export default BarChart;