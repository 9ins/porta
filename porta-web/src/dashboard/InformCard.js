import React, { useState, useEffect, useCallback } from 'react';
import Paper from '@material-ui/core/Paper';
import CardContent from '@material-ui/core/CardContent';
import Grid from '@material-ui/core/Grid';
import {WhiteTextTypography} from '../widget/Widgets';
import { withStyles } from '@material-ui/core/styles';
import { chartCardThemes } from '../app/PortaThemes.js'; 

class ChartCard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            title : props.title,
            direction : props.direction,
            justify : props.justify,
            content : props.content,
        }
        console.log(this.props.content);
    }

    render() {
        const { classes } = this.props;
        const bullet = <span className={classes.bullet}>•</span>;
        return (
            <Grid container className={classes.root} direction='row' justify='center'>
                <Paper className={classes.cardcontent}>                    
                    <Grid>
                        <WhiteTextTypography className={classes.title}>
                            {bullet} {this.props.title}
                        </WhiteTextTypography>
                    </Grid>                
                    <CardContent className={classes.cardcontent}>
                        {JSON.stringify(this.props.content)}
                    </CardContent>
                </Paper>
            </Grid>
        )
    };
}

export default withStyles(chartCardThemes)(ChartCard);

