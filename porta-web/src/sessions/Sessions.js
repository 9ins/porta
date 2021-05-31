import React, { Component } from 'react';
import InformCard from '../dashboard/InformCard';
import Grid from '@material-ui/core/Grid';
import { withStyles, makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';
import '../css/responsive_tabs.css';

class Dashboard extends Component {

    constructor(props) {
        super(props);
        this.state = {
            session : {
                name : 'sessionConfiguration',
                title : ''
            }
        }
    }

    updateSessionInfo = () => {
        this.request.fetchGetResource(this.state.sessionSimple.path)
            .then(json => {
                this.setState({
                    sessionSimpleJson: json,
                    sessionNames: Object.keys(json),
                    sessionStatistics: Object.keys(json).map((e, index) => (json[e].statisticsMap)),
                })
            })
    }

    render() {
        return (
            <Grid container spacing={5} justify='center' className={classes.root}>
                <Grid item xs={12}>
                    <InformCard title={this.state.sessionSimple.title} content={
                        (
                            <TableContainer component={Paper}>
                                <Table className={classes.table} aria-label="simple table">
                                    <TableHead>
                                        <StyledTableRow>
                                            <StyledTableCell>Session Name</StyledTableCell>
                                            <StyledTableCell align="center">SESSION MODE</StyledTableCell>
                                            <StyledTableCell align="center">SESSION TOTAL SUCCESS</StyledTableCell>
                                            <StyledTableCell align="center">SESSION TOTAL FAIL</StyledTableCell>
                                            <StyledTableCell align="center">SESSION SEND SIZE</StyledTableCell>
                                            <StyledTableCell align="center">SESSION RECEIVE SIZE</StyledTableCell>
                                        </StyledTableRow>
                                    </TableHead>
                                    <TableBody>
                                        {console.log(this.state.sessionStatistics)}
                                        {this.state.sessionNames.map((s, idx) => (

                                            <StyledTableRow key={s}>
                                                <StyledTableCell component="th" scope="row">
                                                    <SessionNameTypography>{s.toUpperCase()}</SessionNameTypography>
                                                </StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionStatistics[idx].SESSION_MODE.replaceAll('_', ' ')} </StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionStatistics[idx].SESSION_TOTAL_SUCCESS}</StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionStatistics[idx].SESSION_TOTAL_FAIL}</StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionStatistics[idx].SESSION_SEND_SIZE_TOTAL}</StyledTableCell>
                                                <StyledTableCell align="center">{(this.state.sessionStatistics[idx].SESSION_RECEIVE_SIZE_TOTAL / (1024 * 1024)).toFixed(2)} MB</StyledTableCell>
                                            </StyledTableRow>

                                        ))}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        )
                    }>
                    </InformCard>
                </Grid>
            </Grid>
        )
    }
}