import React, { useState, useEffect, useCallback } from 'react';
import Paper from '@material-ui/core/Paper';
import CardContent from '@material-ui/core/CardContent';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import { withStyles } from '@material-ui/core/styles';
import { chartCardThemes } from '../app/PortaThemes.js'; 

class ChartCard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            title : props.title,
            message : props.message,
            direction : props.direction,
            justify : props.justify,
            content : props.content,
        }
    }

    render() {
        const { classes } = this.props;
        const bullet = <span className={classes.bullet}>•</span>;
        return (
            <Grid container className={classes.root} direction='column' justify={this.state.spacing ? 'center' : this.state.justify} >
                <Paper className={classes.cardcontent}>                    
                    <Grid>
                        <Typography className={classes.cardcontent}>
                            {bullet} {this.props.message}
                        </Typography>
                    </Grid>                
                    <CardContent className={classes.cardcontent}>
                        {this.props.content}
                    </CardContent>
                </Paper>
            </Grid>
        )
    };
}

export default withStyles(chartCardThemes)(ChartCard);

