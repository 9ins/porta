import { withStyles, makeStyles } from '@material-ui/core/styles';
import React, { Component } from 'react';
import InformCard from '../dashboard/InformCard';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';
import { chartCardThemes, sessionsThemes, StyledTableCell, StyledTableRow } from '../app/PortaThemes.js';
import { MessageAlert, PrettoSlider, ApplyButton, WhiteTextTypography, SessionNameTypography } from '../widget/Widgets';
import FetchRequest from '../app/FetchRequest';
import '../css/responsive_tabs.css';

class Sessions extends Component {

    constructor(props) {
        super(props);
        this.state = { 
            sessions : { 
                name: 'sessionsInfo',  
                title: 'Sessions Configuration', 
                path: '/session?type=SESSIONS_INFO', 
            },
            sessionNames: [],
            sessionsInfo: '',
            sessionStatistics: '',
        }
        this.request = new FetchRequest();
    }

    componentDidMount() {
        this.updateSessionInfo();
    }
    
    updateSessionInfo = () => {
        console.log(this.state.sessions.path); 
        this.request.fetchGetResource(this.state.sessions.path)
            .then(json => {
                console.log(json);
                this.setState({
                    sessionNames: Object.keys(json),
                    sessionsInfo: json, 
                    sessionStatistics: Object.keys(json).map((e, index) => json[e].statisticsMap),
                })
            })  
        }
        
    componentWillUnmount() {
      clearInterval(this.setIntervalId);
    }
    
    render() {
        const { classes, card } = this.props;
        const bull = <span className={classes.bullet}>•</span>;
        return (
            <Grid container spacing={5} justify='center' className={classes.root}>
                <Grid item xs={12}>
                    <InformCard title={this.state.sessions.title} content={
                        (
                            <TableContainer component={Paper}>
                                <Table className={classes.table} aria-label="simple table">
                                    <TableHead>
                                        <StyledTableRow>
                                            <StyledTableCell>SESSION NAME</StyledTableCell>
                                            <StyledTableCell align="center">SESSION MODE</StyledTableCell>
                                            <StyledTableCell align="center">SESSION PORT</StyledTableCell>
                                            <StyledTableCell align="center">RETRY</StyledTableCell>
                                            <StyledTableCell align="center">RETRY INTERVAL</StyledTableCell>
                                            <StyledTableCell align="center">REMOTES</StyledTableCell>
                                            <StyledTableCell align="center">ALLOWED</StyledTableCell>
                                            <StyledTableCell align="center">LOADBALANCE RATIO</StyledTableCell>
                                            <StyledTableCell align="center">FORBIDDEN</StyledTableCell>
                                            <StyledTableCell align="center">BUFFER</StyledTableCell>
                                            <StyledTableCell align="center">CONNECTION TIMEOUT</StyledTableCell>
                                            <StyledTableCell align="center">KEEP ALIVE</StyledTableCell>
                                            <StyledTableCell align="center">SO TIMEOUT</StyledTableCell>
                                            <StyledTableCell align="center">TCP NO DELAY</StyledTableCell>
                                        </StyledTableRow>
                                    </TableHead>
                                    <TableBody>
                                        {this.state.sessionNames.map((s, index) => ( 
                                            <StyledTableRow key={s}>
                                                <StyledTableCell component="th" scope="row">
                                                    <SessionNameTypography>{s.toUpperCase()}</SessionNameTypography>
                                                </StyledTableCell>
                                                {console.log(this.state.sessionsInfo[s]['tcpNoDelay'])}
                                                <StyledTableCell className={classes.text_blue} align="center">{this.state.sessionsInfo[s]['sessionMode']}</StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionsInfo[s]['port']}</StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionsInfo[s]['retry']}</StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionsInfo[s]['retryInterval']} ms</StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionsInfo[s]['remoteHosts'][0]}</StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionsInfo[s]['allowedHosts'][0]}</StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionsInfo[s]['loadBalanceRatio']}</StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionsInfo[s]['forbiddenHosts'][0]}</StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionsInfo[s]['bufferSize']}</StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionsInfo[s]['connectionTimeout']}</StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionsInfo[s]['keepAlive']+''}</StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionsInfo[s]['soTimeout']}</StyledTableCell>
                                                <StyledTableCell align="center">{this.state.sessionsInfo[s]['tcpNoDelay']+''}</StyledTableCell>
                                            </StyledTableRow>

                                        ))}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        )
                    }>
                    </InformCard>
                </Grid>
                <Grid item xs={12}>
                    
                </Grid>
            </Grid>
        )
    }
}

export default withStyles(sessionsThemes)(Sessions);


