import React, { Component } from 'react';
import ChartCard from './ChartCard';
import LineChart from './LineChart';
import BarChart from './BarChart';
import randomColor from 'randomcolor';
import InformCard from './InformCard';
import Box from '@material-ui/core/Box';
import Grid from '@material-ui/core/Grid';
import { withStyles, makeStyles } from '@material-ui/core/styles';
import DoneOutlinedIcon from '@material-ui/icons/DoneOutlined';
import Typography from '@material-ui/core/Typography';
import { Snackbar, Button } from '@material-ui/core';
import { chartCardThemes, dashboardThemes, StyledAutocomplete, StyledTextField, StyledTableContainer, StyledTableCell, StyledTableRow, StyledTypography} from '../app/PortaThemes.js';
import { MessageAlert, PrettoSlider, ApplyButton, WhiteTextTypography, SessionNameTypography } from '../widget/Widgets';
import FetchRequest from '../app/FetchRequest';
import Paper from '@material-ui/core/Paper';
import { AutoSizer } from 'react-virtualized';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';
import TextField from '@material-ui/core/TextField';
import InputAdornment from '@material-ui/core/InputAdornment';
import AccountCircle from '@material-ui/icons/AccountCircle';
import Autocomplete from '@material-ui/lab/Autocomplete';
import '../css/responsive_tabs.css';

class Dashboard extends Component {

  constructor(props) {
    super(props);
    this.state = {
      memory: {
        name: 'memory',
        title: 'Memory status',
        path: '/resources?type=MEMORY&unit=GB',
        elements: ['systemUsed', 'heapFree', 'memoryUsed'],
        labels: ['system', 'heapFree', 'memory'],
        dimension: [400, 250],
        spacing: 5,
        direction: 'column',
        unit: 'GB',
        content: (
          <div>
            <WhiteTextTypography variant='body2'>system: total memory usage of system.</WhiteTextTypography>
            <WhiteTextTypography variant='body2'>heapFree: Porta heap free memory(Max - used).</WhiteTextTypography>
            <WhiteTextTypography variant='body2'>memory: Porta used memory currently.</WhiteTextTypography>
          </div>
        )
      },
      cpu: {
        name: 'cpu',
        title: 'CPU status',
        path: '/resources?type=CPU&unit=PCT',
        elements: ['cpuLoad', 'systemCpuLoad'],
        labels: ['cpu', 'systemCpu'],
        dimension: [400, 250],
        spacing: 5,
        direction: 'column',
        unit: '%',
      },
      threadPool: {
        name: 'threadPool',
        title: 'Thread pool status',
        path: '/resources?type=THREAD&unit=CNT',
        elements: ['activeCount', 'corePoolSize', 'maxinumPoolSize', 'queueSize'],
        labels: ['active', 'core', 'max', 'queue'],
        dimension: [250, 275],
        yDomain: 220,
        xDistance: 0,
        yDistance: 0,
        path1: '/resources?type=THREAD_POOL',
        corePoolSize: 0,
        maximumPoolSize: 0,
        limitPoolSize: 0,
        queueSize: 0,
      },
      sessionInfo: {
        sessionNames: [],
        sessionSimpleJson: '',
        sessionStatistics: '',
        sessionSimple: {
          name: 'sessionSimple',
          title: 'Session Information',
          path: '/session?type=SESSION_SIMPLE',
        },
      },
      alert: {
        open: false,
        message: '',
        status: '',
      },
      dashboardRefreshSec: 2,  
      sessionColumn: ['SESSION NAME', 
                      'SESSION MODE', 
                      'SESSION TOTAL SUCCESS', 
                      'SESSION TOTAL FAIL', 
                      'SESSION SEND SIZE', 
                      'SESSION RECEIVE SIZE',],
    }
    this.request = new FetchRequest();
  }

  componentDidMount() {
    this.request.fetchGetResource(this.state.threadPool.path)
      .then(json => {
        this.setState({
          threadpool : {
            corePoolSize: json['corePoolSize'],
            maximumPoolSize: json['maxinumPoolSize'],
            limitPoolSize: json['limitPoolSize'],
            queueSize: json['queueSize'],
          }
        });
      })    
    this.updateSessionInfo();
    this.setIntervalId = setInterval(() => {
      this.updateSessionInfo();
    }, this.state.dashboardRefreshSec * 1000);
  }

  updateSessionInfo = () => {
    this.request.fetchGetResource(this.state.sessionInfo.sessionSimple.path)
    .then(json => {
        this.setState({
          sessionInfo : {
            sessionSimpleJson: json,
            sessionNames: Object.keys(json),
            sessionStatistics: Object.keys(json).map((e, idx) => {
              var map  = json[e].statisticsMap;
              map.NAME = e;
              return map;
            }),  
            sessionSimple: {
              name: 'sessionSimple',
              title: 'Session Information',
              path: '/session?type=SESSION_SIMPLE',
            },    
          }
        })
        console.log(this.state.sessionInfo.sessionStatistics);
      })  
    }

  componentWillUnmount() {
    clearInterval(this.setIntervalId);
  }

  handleCorePoolSize = (v) => {
    this.setState({
      corePoolSize: v,
    });
  }

  handleMaximumPoolSize = (v) => {
    this.setState({
      maximumPoolSize: v,
    });
  }

  handleApply = (event) => {
    if (this.state.threadpool.corePoolSize <= this.state.threadpool.maximumPoolSize) {
      const body = {
        corePoolSize: this.state.threadpool.corePoolSize,
        maximumPoolSize: this.state.threadpool.maximumPoolSize,
      }
      this.res = this.request.fetchPostRequestAsync(this.state.threadpool.path1, body);
      this.res.then(json => {        
        if (json != null && json['status'] === 'success') {
          event.open = true;
          event.message = json['message'];
          event.status = json['status'];
          this.handleAlertOpen(event);
        }
      })
    }
  }

  stableSort(array, comparator) {
    const stabilizedThis = array.map((el, index) => [el, index]);
    stabilizedThis.sort((a, b) => {
      const order = comparator(a[0], b[0]);
      if (order !== 0) return order;
      return a[1] - b[1];
    });
    return stabilizedThis.map((el) => el[0]);
  }

  handleAlertOpen = (event) => {
    this.setState({
      alert: {
        open: event.open,
        message: event.message,
        status: event.status,
      }
    })
  };

  handleAlertClose = (event) => {
    this.setState({
      alert: {
        open: false,
        message: '',
        status: ''
      }
    })
  };

  render() {
    const { classes, card } = this.props;
    const bull = <span className={classes.bullet}>•</span>;
    
    return (
      <div>
      <Snackbar open={this.state.alert.open} autoHideDuration={3000} onClose={this.handleAlertClose}>
        <MessageAlert className={classes.alert}
          serverity={this.state.alert.status}
          elevation={10}
          variant="filled"
          onClose={this.handleAlertClose} >{this.state.alert.message}</MessageAlert>
      </Snackbar>
      <Grid container spacing={5} justify='center' className={classes.root}>
        <Grid item xs>
          <ChartCard content={this.state.memory.content} medias={(
            <LineChart name={this.state.memory.name}
              title={this.state.memory.title}
              path={this.state.memory.path}
              elements={this.state.memory.elements}
              labels={this.state.memory.labels}
              color={[randomColor(), randomColor(), randomColor()]}
              dimension={this.state.memory.dimension}
              refreshSec={this.state.dashboardRefreshSec}
              unit={this.state.memory.unit}
            />)}
          />
        </Grid>
        <Grid item xs>
          <ChartCard medias={(
            <LineChart name={this.state.cpu.name}
              title={this.state.cpu.title}
              path={this.state.cpu.path}
              elements={this.state.cpu.elements}
              labels={this.state.cpu.labels}
              color={[randomColor(), randomColor()]}
              dimension={this.state.cpu.dimension}
              refreshSec={this.state.dashboardRefreshSec}
              unit={this.state.cpu.unit}
            />)}
          />
        </Grid>
        <Grid item xs>
          <ChartCard medias={(
            <Grid>
              <Grid key={0} item>
                <BarChart name={this.state.threadPool.name}
                  title={this.state.threadPool.title}
                  path={this.state.threadPool.path}
                  elements={this.state.threadPool.elements}
                  labels={this.state.threadPool.labels}
                  color={[randomColor(), randomColor(), randomColor(), randomColor()]}
                  dimension={this.state.threadPool.dimension}
                  refreshSec={this.state.dashboardRefreshSec} />
              </Grid>
              <Grid item>
                <Grid item>
                  <Typography className={classes.text} id='pretto-slider' gutterBottom>
                    Core Thread Size
                </Typography>
                  <PrettoSlider valueLabelDisplay='auto'
                    aria-labelledby='range-slider'
                    value={this.state.threadPool.corePoolSize}
                    max={this.state.threadPool.maximumPoolSize}
                    onChange={(event, v) => { this.handleCorePoolSize(v) }} />
                </Grid>
                <Grid item>
                  <Typography className={classes.text} id='pretto-slider' gutterBottom>
                    Max Thread Size
                  </Typography>
                  <PrettoSlider valueLabelDisplay='auto'
                    aria-labelledby='range-slider'
                    value={this.state.threadPool.maximumPoolSize}
                    max={this.state.threadPool.limitPoolSize}
                    onChange={(event, v) => { this.handleMaximumPoolSize(v) }} />
                  <ApplyButton startIcon={<DoneOutlinedIcon />} onClick={(event, v) => { this.handleApply(event) }}> Apply </ApplyButton>
                </Grid>
              </Grid>
            </Grid>
          )}
          />
        </Grid>
        <Grid item xs={12}>          
          <InformCard title={this.state.sessionInfo.sessionSimple.title} content={
            (
              <div>
              <StyledTableContainer component={Paper}>
              <Box m={2}>
                <Grid container spacing={3} justify='left' style={{width: 1400}}>
                  <Grid item xs={3}>
                  <StyledAutocomplete 
                      id="size-small-standard" 
                      options={this.state.sessionColumn} 
                      getOptionLabel={(option) => option} 
                      defaultValue={this.state.sessionColumn[0]}
                      renderOption={(option) => (
                        <StyledTypography>{option}</StyledTypography>
                      )}
                      renderInput={(params) => (
                        <StyledTextField {...params} id="standard-size-small" label="Column" variant="outlined" />
                    )}/>
                  </Grid>
                  <Grid item xs={3}>
                    <StyledTextField id="standard-size-small" label="Search" variant="outlined" />
                  </Grid>
                </Grid>
              </Box>
              <Table className={classes.table} aria-label="simple table">
                <TableHead>
                  <StyledTableRow> 
                    {
                      this.state.sessionColumn.map((s, idx) => (
                        <StyledTableCell key={idx} align="center">{s}</StyledTableCell>
                      ))
                    }
                  </StyledTableRow>
                </TableHead>
                <TableBody>
                  {console.log(this.state.sessionInfo.sessionStatistics)}
                  {                    
                    this.state.sessionInfo.sessionNames.map((s, idx) =>                   
                      (              
                      <StyledTableRow key={s}>
                        <StyledTableCell component="th" scope="row">
                          <SessionNameTypography>{s.toUpperCase()}</SessionNameTypography>
                        </StyledTableCell>
                        <StyledTableCell align="center">{this.state.sessionInfo.sessionStatistics[idx].SESSION_MODE.replaceAll('_', ' ')} </StyledTableCell>
                        <StyledTableCell align="center">{this.state.sessionInfo.sessionStatistics[idx].SESSION_TOTAL_SUCCESS}</StyledTableCell>
                        <StyledTableCell align="center">{this.state.sessionInfo.sessionStatistics[idx].SESSION_TOTAL_FAIL}</StyledTableCell>
                        <StyledTableCell align="center">{ (this.state.sessionInfo.sessionStatistics[idx].SESSION_SEND_SIZE_TOTAL / (1024 * 1024)).toFixed(2)} MB</StyledTableCell>
                        <StyledTableCell align="center">{ (this.state.sessionInfo.sessionStatistics[idx].SESSION_RECEIVE_SIZE_TOTAL / (1024 * 1024)).toFixed(2)} MB</StyledTableCell>
                      </StyledTableRow>
                      ))
                  }
                </TableBody>
              </Table>
            </StyledTableContainer>           
            </div>     
            )
          }>
          </InformCard>
        </Grid>
      </Grid>
    </div>
    )
  }
}

export default withStyles(dashboardThemes)(Dashboard);