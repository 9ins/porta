import React, { Component } from 'react';
import '../../node_modules/react-vis/dist/style.css';
import {VerticalGridLines, HorizontalGridLines, XAxis, YAxis, XYPlot, LineSeries} from 'react-vis';
import randomColor from 'randomcolor'

class LineChart extends Component {

    constructor(props) {
        super(props); 
        this.state = {
            data : props.element.map(e => [{x:0, y:0}])
        }
    }    

    componentDidMount() { 
        this.setIntervalId = setInterval(() => {
            const requestOptions = {
                method: 'GET',
                headers: { 
                    'Content-Type': 'application/json',
                    'username' : 'admin'
                },
            }; 
            fetch("/resources?type="+this.props.type+"&unit="+this.props.unit, requestOptions)
            .then(response => response.json())
            .then(json => {
                if(this.state.data.length > 30) {                    
                    var valArr = this.props.element.map((e, index) => json[e]);
                    this.setState({
                        data : this.props.element.map((e, index) => this.state.data[index].slice(1, this.state.data[index].length).map((e, index) => ({x : index, y : e.y})))
                    })                    
                }
                this.setState({
                    data : this.props.element.map((e, index) => [...this.state.data[index], {x: this.state.data[index][this.state.data[index].length-1].x+1, y: json[e]}]
                    )
                })
            });
        }, 3000);        
    }

    componentWillUnmount() {
        clearInterval(this.setIntervalId);
    }

    render() {        
        return (        
            <div className="LineChart" style={
                {
                    backgroundColor: "#f6f5f5",
                    border: '2px solid darkgreen',
                    width: this.props.width,
                    height: this.props.height,
                }
            }>
                <XYPlot width={this.props.width} height={this.props.height} stroke="#5ecca8" xDomain={[0,30]} yDomain={this.props.domain} >
                <VerticalGridLines />
                <HorizontalGridLines />
                <XAxis />
                <YAxis />            
                {
                    this.props.element.map((e, index) => (
                        <LineSeries data={this.state.data[index]} stroke={this.props.stroke[index]} />
                    ))
                }
            </XYPlot>
            </div>
        );
    }
}

export default LineChart;