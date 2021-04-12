import React, { Component } from 'react';
import { VerticalBarSeries, XAxis, XYPlot, DiscreteColorLegend } from 'react-vis';
import FetchRequest from '../app/FetchRequest';
import { withStyles } from '@material-ui/core/styles';
import {chartThemes} from '../app/PortaThemes';
import { CardContent, isWidthDown } from '@material-ui/core';

class BarChart extends Component {

    constructor(props) {
        super(props);
        this.state = {
            data: props.elements.map((e, index) => [{
                x: props.labels[index],
                y: 0
            }]),
        }
        this.fetchRequest = new FetchRequest().fetchGetBarchart;
    }

    componentDidMount() {
        this.fetchRequest(this.props.path, this);
        this.setIntervalId = setInterval(() => {
            this.fetchRequest(this.props.path, this);
        },  this.props.refreshSec * 1000);
    }

    componentWillUnmount() {
        clearInterval(this.setIntervalId);
    }

    render() {
        console.log("////////// "+this.state.width);
        const BarSeries = VerticalBarSeries;
        const { classes } = this.props;
        const bullet = <span className={classes.bullet}>•</span>;
        return (
            <CardContent className={classes.root}>
                <DiscreteColorLegend orientation='horizontal' items={this.props.elements.map((e, index) => (
                    {
                        title: this.props.labels[index],
                        color: this.props.color[index],
                    }
                ))} />
                <XYPlot xType="ordinal" width={this.props.dimension[0]} height={this.props.dimension[1]} xDistance={5}>
                    <XAxis />
                    {
                        this.props.elements.map((e, index) => (
                            <BarSeries animation key={index} data={this.state.data[index]} color={this.props.color[index]} />
                        ))
                    }
                </XYPlot>
            </CardContent>
        );
    }
}

export default withStyles(chartThemes)(BarChart);