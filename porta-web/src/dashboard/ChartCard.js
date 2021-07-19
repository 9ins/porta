import React, { useState, useEffect, useCallback } from 'react';
import Paper from '@material-ui/core/Paper';
import CardContent from '@material-ui/core/CardContent';
import Grid from '@material-ui/core/Grid';
import { withStyles } from '@material-ui/core/styles';
import { chartCardThemes } from '../app/PortaThemes.js'; 
import { Typography } from '@material-ui/core';

class ChartCard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            medias: props.medias,
            content: props.content,
        }        
    }

    componentDidMount() {
        this.setState({
            width: this.paperRef.clientWidth,
            height: this.paperRef.clientHeight
        });
    }

    render() {
        const { classes } = this.props;
        const bullet = <span className={classes.bullet}>•</span>;
        return (
                <Paper className={classes.paper} ref={(paperRef) => {this.paperRef = paperRef}}>
                    <CardContent className={classes.cardMedia} title={this.props.title}>
                        {this.props.medias}
                    </CardContent>
                    <CardContent className={classes.cardcontent} width={this.state.width} height={this.state.height}>
                        {
                            this.props.content
                        }
                    </CardContent>
                </Paper>
        )
    };
}

export default withStyles(chartCardThemes)(ChartCard);

