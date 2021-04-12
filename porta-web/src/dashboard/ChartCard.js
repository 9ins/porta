import React, { useState, useEffect, useCallback } from 'react';
import Paper from '@material-ui/core/Paper';
import CardContent from '@material-ui/core/CardContent';
import Grid from '@material-ui/core/Grid';
import { withStyles } from '@material-ui/core/styles';
import { chartCardThemes } from '../app/PortaThemes.js'; 

class ChartCard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            direction: props.direction,
            justify: props.justify,
            medias: props.medias,
            content: props.content,
        }
    }

    render() {
        const { classes } = this.props;
        const bullet = <span className={classes.bullet}>•</span>;
        return (
                <Paper className={classes.paper}>
                    <CardContent className={classes.cardcontent} title={this.props.title}>
                        {this.props.medias}
                    </CardContent>
                    <CardContent className={classes.cardcontent}>
                        {this.props.content}
                    </CardContent>
                </Paper>
        )
    };
}

export default withStyles(chartCardThemes)(ChartCard);

