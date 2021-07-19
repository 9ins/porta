import React, { Component } from 'react';
import { VerticalGridLines, HorizontalGridLines, XAxis, YAxis, XYPlot, LineSeries, DiscreteColorLegend } from 'react-vis';
import FetchRequest from '../app/FetchRequest';
import Typography from '@material-ui/core/Typography';
import {chartThemes} from '../app/PortaThemes';
import { withStyles } from '@material-ui/core/styles';
import { CardContent, isWidthDown } from '@material-ui/core';
import '../../node_modules/react-vis/dist/style.css';

class LineChart extends Component {

    constructor(props) {
        super(props);
        this.state = {
            xDomain: [0, 100], 
            yDomain: [0, 100], 
            data: props.elements.map((e, index) => [{ x: 0, y: 0 }])
        }        
        this.fetchRequest = new FetchRequest().fetchGetLinechart;
    }

    componentDidMount() {
        this.fetchRequest(this.props.path, this);
        this.setIntervalId = setInterval(() => {
            this.fetchRequest(this.props.path, this);
        }, this.props.refreshSec * 1000);
    }

    componentWillUnmount() {
        clearInterval(this.setIntervalId);
    }

    render() {
        const { classes } = this.props;
        const bullet = <span className={classes.bullet}>•</span>;
        return (
            <CardContent className={classes.root}>
                <Typography className={classes.title}>
                    {bullet} {this.props.title}
                </Typography>
                <DiscreteColorLegend className={classes.legend} orientation='horizontal' items={this.props.labels.map((e, index) => ({
                        title: e,
                        color: this.props.color[index]
                    }))
                } />
                <XYPlot className={classes.xy} width={this.props.dimension[0]} height={this.props.dimension[1]} xDomain={this.state.xDomain} yDomain={this.state.yDomain} >
                    <VerticalGridLines />
                    <HorizontalGridLines />
                    {/* <XAxis tickFormat={(d) => ''} */}
                    <YAxis tickFormat={(d) => d + '' + ((this.props.name === 'cpu') ? '%' : this.props.unit)} />
                    {
                        this.props.elements.map((e, index) => (
                            <LineSeries key={index} data={this.state.data[index]} style={{ stroke: this.props.color[index], strokeWidth: 1.5 }} />
                        ))
                    }
                </XYPlot>
            </CardContent>
        );
    }
}

export default withStyles(chartThemes)(LineChart);