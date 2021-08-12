import React, { Component } from 'react';

import { withStyles, makeStyles } from '@material-ui/core/styles';
import { MenuItem, InputAdornment, TextField, TableRow, TableCell, TableHead, TableContainer, TableBody, Table, Paper, Snackbar, Button, Box, Grid, Typography} from '@material-ui/core';
import CheckOutlinedIcon from '@material-ui/icons/CheckOutlined';

import ChartCard from './ChartCard';
import LineChart from './LineChart';
import BarChart from './BarChart';
import randomColor from 'randomcolor';
import InformCard from './InformCard';
import FetchRequest from '../app/FetchRequest';
import '../css/responsive_tabs.css';
import { chartCardThemes, dashboardThemes, StyledSelect, StyledTextField, StyledTableContainer, StyledTableCell, StyledTableRow, StyledTypography} from '../app/PortaThemes.js';
import { MessageAlert, PrettoSlider, ApplyButton, WhiteTextTypography, SessionNameTypography } from '../widget/Widgets';


class Dashboard extends Component {

  constructor(props) {
      super(props);
      this.state = {
        memory_name: 'memory',
        memory_title: 'Memory status',
        memory_path: '/resources?type=MEMORY&unit=GB',
        memory_elements: ['systemUsed', 'heapFree', 'memoryUsed'],
        memory_labels: ['system', 'heapFree', 'memory'],
        memory_dimension: [400, 250],
        memory_spacing: 5,
        memory_direction: 'column',
        memory_unit: 'GB',
        memory_content: (
          <div>
            <WhiteTextTypography variant='body2'>system: total memory usage of system.</WhiteTextTypography>
            <WhiteTextTypography variant='body2'>heapFree: Porta heap free memory(Max - used).</WhiteTextTypography>
            <WhiteTextTypography variant='body2'>memory: Porta used memory currently.</WhiteTextTypography>
          </div>
        ),
        cpu_name: 'cpu',
        cpu_title: 'CPU status',
        cpu_path: '/resources?type=CPU&unit=PCT',
        cpu_elements: ['cpuLoad', 'systemCpuLoad'],
        cpu_labels: ['cpu', 'systemCpu'],
        cpu_dimension: [400, 250],
        cpu_spacing: 5,
        cpu_direction: 'column',
        cpu_unit: '%',
        threadpool_name: 'threadpool',
        threadpool_title: 'Thread pool status',
        threadpool_path: '/resources?type=THREAD&unit=CNT',
        threadpool_elements: ['activeCount', 'corePoolSize', 'maxinumPoolSize', 'queueSize'],
        threadpool_labels: ['active', 'core', 'max', 'queue'],
        threadpool_dimension: [250, 275],
        threadpool_yDomain: 220,
        threadpool_xDistance: 0,
        threadpool_yDistance: 0,
        threadpool_path1: '/resources?type=THREAD_POOL',
        threadpool_corePoolSize: 0,
        threadpool_maximumPoolSize: 0,
        threadpool_limitPoolSize: 0,
        threadpool_queueSize: 0,
        sessioninfo_sessionNames: [],
        sessioninfo_sessionSimpleJson: '',
        sessioninfo_sessionStatistics: '',
        sessioninfo_sessionSimple: {
            name: 'sessionSimple',
            title: 'Session Information',
            path: '/session?type=SESSION_SIMPLE',
        alert_open: false,
        alert_message: '',
        alert_status: '',
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
      this.request.fetchGetResource(this.state.threadpool_path)
      .then(json => {
          this.setState({
              threadpool_corePoolSize: json['corePoolSize'],
              threadpool_maximumPoolSize: json['maxinumPoolSize'],
              threadpool_limitPoolSize: json['limitPoolSize'],
              threadpool_queueSize: json['queueSize'],
          });
      })    
      this.updateSessionInfo();
      this.setIntervalId = setInterval(() => {
          this.updateSessionInfo();
      }, this.state.dashboardRefreshSec * 1000);
  }

  updateSessionInfo = () => {
      this.request.fetchGetResource(this.state.sessioninfo_sessionSimple.path)
      .then(json => {
          this.setState({
              sessioninfo_sessionSimpleJson: json,
              sessioninfo_sessionNames: Object.keys(json),
              sessioninfo_sessionStatistics: Object.keys(json).map((e, idx) => {
                  var map  = json[e].statisticsMap;
                  map.NAME = e;
                  return map;
              }),  
              sessioninfo_sessionSimple: {
                  name: 'sessionSimple',
                  title: 'Session Information',
                  path: '/session?type=SESSION_SIMPLE',
              },    
          })
          console.log(this.state.sessionInfo_sessionStatistics);
      })  
  }

  componentWillUnmount() {
      clearInterval(this.setIntervalId);
  }

  handleCorePoolSize = (v) => {
      this.setState({
          threadpool_corePoolSize: v,
      });
  }

  handleMaximumPoolSize = (v) => {
      this.setState({
          threadpool_maximumPoolSize: v,
      });
  }

  handleApply = (event) => {
      if (this.state.threadpool_corePoolSize <= this.state.threadpool_maximumPoolSize) {
          const body = {
              corePoolSize: this.state.threadpool_corePoolSize,
              maximumPoolSize: this.state.threadpool_maximumPoolSize,
          }
          this.res = this.request.fetchPostRequestAsync(this.state.threadpool_path1, body);
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
  
  handleSearch = (v) => {

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
          alert_open: event.open,
          alert_message: event.message,
          alert_status: event.status,
      })
  };

  handleAlertClose = (event) => {
      this.setState({
          alert_open: false,
          alert_message: '',
          alert_status: ''
      })
  };

  render() {
      const { classes, card } = this.props;
      const bull = <span className={classes.bullet}>•</span>;
    
      return (
          <div>
              <Snackbar open={this.state.alert_open} autoHideDuration={3000} onClose={this.handleAlertClose}>
                  <MessageAlert className={classes.alert}
                                serverity={this.state.alert_status}
                                elevation={10}
                                variant="filled"
                                onClose={this.handleAlertClose} >{this.state.alert_message}
                  </MessageAlert>
              </Snackbar>
              <Grid container spacing={5} justify='center' className={classes.root}>
                  <Grid item xs>
                      <ChartCard content={this.state.memory_content} medias={(
                                <LineChart name={this.state.memory_name}
                                          title={this.state.memory_title}
                                          path={this.state.memory_path}
                                          elements={this.state.memory_elements}
                                          labels={this.state.memory_labels}
                                          color={[randomColor(), randomColor(), randomColor()]}
                                          dimension={this.state.memory_dimension}
                                          refreshSec={this.state.dashboardRefreshSec}
                                          unit={this.state.memory_unit} />)}
                      />
                  </Grid>
                  <Grid item xs>
                      <ChartCard medias={(
                                <LineChart name={this.state.cpu_name}
                                          title={this.state.cpu_title}
                                          path={this.state.cpu_path}
                                          elements={this.state.cpu_elements}
                                          labels={this.state.cpu_labels}
                                          color={[randomColor(), randomColor()]}
                                          dimension={this.state.cpu_dimension}
                                          refreshSec={this.state.dashboardRefreshSec}
                                          unit={this.state.cpu_unit} />)}
                      />
                  </Grid>
                  <Grid item xs>
                      <ChartCard medias={(
                          <Grid>
                              <Grid key={0} item>
                                  <BarChart name={this.state.threadpool_name}
                                            title={this.state.threadpool_title}
                                            path={this.state.threadpool_path}
                                            elements={this.state.threadpool_elements}
                                            labels={this.state.threadpool_labels}
                                            color={[randomColor(), randomColor(), randomColor(), randomColor()]}
                                            dimension={this.state.threadpool_dimension}
                                            refreshSec={this.state.dashboardRefreshSec} />
                              </Grid>
                              <Grid item>
                              <Grid item>
                                  <Typography className={classes.text} id='pretto-slider' gutterBottom>Core Thread Size</Typography>
                                  <PrettoSlider valueLabelDisplay='auto'
                                            aria-labelledby='range-slider'
                                            value={this.state.threadpool_corePoolSize}
                                            max={this.state.threadpool_maximumPoolSize}
                                            onChange={(event, v) => { this.handleCorePoolSize(v) }} />
                              </Grid>
                              <Grid item>
                                    <Typography className={classes.text} id='pretto-slider' gutterBottom>Max Thread Size</Typography>
                                    <PrettoSlider valueLabelDisplay='auto'
                                              aria-labelledby='range-slider'
                                              value={this.state.threadpool_maximumPoolSize}
                                              max={this.state.threadpool_limitPoolSize}
                                              onChange={(event, v) => { this.handleMaximumPoolSize(v) }} />
                                    <ApplyButton startIcon={<CheckOutlinedIcon />} 
                                              onClick={(event, v) => { this.handleApply(event) }}> 
                                              Apply 
                                    </ApplyButton>
                                  </Grid>
                              </Grid>
                          </Grid>
                      )} />
                  </Grid>
                  <Grid item xs={12}>          
                      <InformCard title={this.state.sessioninfo_sessionSimple.title} content={(
                          <div>
                               <StyledTableContainer component={Paper}>
                                  <Box m={2}>
                                      <Grid container spacing={3} justify='left' style={{width: 600}}>
                                          <Grid item xs={3}>
                                              <StyledSelect
                                                  labelId="demo-simple-select-label"
                                                  id="demo-simple-select"
                                                  value={this.state.sessionColumn[0]} >
                                                    {
                                                        this.state.sessionColumn.map((s, idx) => (
                                                            <MenuItem style={{fontSize: 12}} key={idx} value={s}>{s}</MenuItem>
                                                        ))
                                                    }
                                              </StyledSelect>
                                          </Grid>
                                          <Grid item xs={3}>
                                              <StyledTextField style={{fontSize: 12}} id="standard-size-small" label="Search" variant="outlined" onChange={v => {this.handleSearch(v)}}/>
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
                                          {
                                              console.log(this.state.sessioninfo_sessionStatistics)
                                          }
                                          {                    
                                              this.state.sessioninfo_sessionNames.map((s, idx) =>                   
                                                  (
                                                      <StyledTableRow key={s}>
                                                          <StyledTableCell component="th" scope="row">
                                                              <SessionNameTypography>{s.toUpperCase()}</SessionNameTypography>
                                                          </StyledTableCell>
                                                      <StyledTableCell align="center">{this.state.sessioninfo_sessionStatistics[idx].SESSION_MODE.replaceAll('_', ' ')} </StyledTableCell>
                                                      <StyledTableCell align="center">{this.state.sessioninfo_sessionStatistics[idx].SESSION_TOTAL_SUCCESS}</StyledTableCell>
                                                      <StyledTableCell align="center">{this.state.sessioninfo_sessionStatistics[idx].SESSION_TOTAL_FAIL}</StyledTableCell>
                                                      <StyledTableCell align="center">{ (this.state.sessioninfo_sessionStatistics[idx].SESSION_SEND_SIZE_TOTAL / (1024 * 1024)).toFixed(2)} MB</StyledTableCell>
                                                      <StyledTableCell align="center">{ (this.state.sessioninfo_sessionStatistics[idx].SESSION_RECEIVE_SIZE_TOTAL / (1024 * 1024)).toFixed(2)} MB</StyledTableCell>
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