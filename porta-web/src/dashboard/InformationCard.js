import React, { useState, useEffect, useCallback } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid'
import { withStyles } from '@material-ui/core/styles';

const styles = theme => ({
    root: {
        backgroundColor: "#bbcec8",
        minWidth: 275,
    },
    bullet: {
        display: 'inline-block',
        margin: '0 1px',
        transform: 'scale(0.8)',
    },
    title: {
        fontSize: 14,
        fontStyle: "bold"
    },
    pos: {
        marginBottom: 8,
    },
});

class InfomationCard extends React.Component {

    constructor(props) {
        super(props);
        this.props = props;
        this.title = this.props.title;
        this.content = this.props.content;
        this.media = this.props.media;
    }
        
    render() {
        const { classes } = this.props;
        const bull = <span className={classes.bullet}>•</span>;
        return (
            <Card className={classes.root}>
                <CardContent>
                    <Typography className={classes.title} color="textSecondary" gutterBottom>
                        {bull} {this.props.title}
                    </Typography>
                    <Typography variant="h5" component="h2">
                        {this.props.content}
                    </Typography>
                </CardContent>
                <CardContent>
                    {this.props.media}
                </CardContent>
            </Card>
        )
    };
}
export default withStyles(styles)(InfomationCard);