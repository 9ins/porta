import React, { Component } from 'react';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import { withStyles } from '@material-ui/core/styles';
import {ApplyButton, PrettoSlider} from '../widget/Widgets'
import { chartCardThemes } from '../app/PortaThemes'

class SimpleSlider extends Component {
    
    constructor(props) {
        super(props);
        this.state = {
            title: props.title,
            min: props.min ? '0' : props.min,
            max: props.max ? '100' : props.max,
            value: props.value ? '0' : props.value,
        }
        console.log(this.state);
    }

    componentDidMount() {
    }
  
    componentWillUnmount() {
    }
  
    handleChange = (event, newValue) => {
      this.setState({ value: newValue });
    }

    valuetext(value) {
      return `${value}°C`;
    }    
  
    render() {
        const { classes } = this.props;
        return (
            <div className={classes.root}>
            </div>            
        )
    }
}        

export default withStyles(chartCardThemes)(SimpleSlider);