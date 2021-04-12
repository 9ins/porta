import React, { Component } from 'react';
import ChartCard from './ChartCard';
import LineChart from './LineChart';
import BarChart from './BarChart';
import randomColor from 'randomcolor';
import InformCard from './InformCard';
import Box from '@material-ui/core/Box';
import Grid from '@material-ui/core/Grid';
import { withStyles } from '@material-ui/core/styles';
import DoneOutlinedIcon from '@material-ui/icons/DoneOutlined';
import Typography from '@material-ui/core/Typography';
import { Snackbar, Button } from '@material-ui/core';
import { chartCardThemes, dashboardThemes } from '../app/PortaThemes.js';
import { PrettoSlider, ApplyButton } from '../widget/Widgets';
import FetchRequest from '../app/FetchRequest';
import { MessageAlert } from '../widget/Widgets'
import Paper from '@material-ui/core/Paper';
import {AutoSizer} from 'react-virtualized';


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
        refreshSec: 3,
        dimension: [400, 250],
        spacing: 5,
        direction: 'column',
        unit: 'GB',
        content: 'This is memory status...',
      },
      cpu: {
        name: 'cpu',
        title: 'CPU status',
        path: '/resources?type=CPU&unit=PCT',
        elements: ['cpuLoad', 'systemCpuLoad'],
        labels: ['cpu', 'systemCpu'],
        refreshSec: 3,
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
        refreshSec: 3,
        xDistance: 0,
        yDistance: 0,
      },
      alert: {
        open: false,
        message: '',
        status: '',
      },
      path: '/resources?type=THREAD_POOL',
      corePoolSize: 0,
      maximumPoolSize: 0,
      limitPoolSize: 0,
      queueSize: 0,
    }
    this.request = new FetchRequest();
    console.log(window.innerWidth+"   "+window.innerWidth * 0.3);
  }

  componentDidMount() {
    this.request.fetchGetResource(this.state.threadPool.path)
      .then(json => {
        this.setState({
          corePoolSize: json['corePoolSize'],
          maximumPoolSize: json['maxinumPoolSize'],
          limitPoolSize: json['limitPoolSize'],
          queueSize: json['queueSize'],
        });
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

  handleApply = () => {
    if (this.state.corePoolSize <= this.state.maximumPoolSize) {
      const body = {
        corePoolSize: this.state.corePoolSize,
        maximumPoolSize: this.state.maximumPoolSize,
      }
      this.res = this.request.fetchPostRequest(this.state.path, body);
      this.res.then(json => {
        console.log(json);
        if (json['status'] === 'success') {
          this.setState({
            alert: {
              open: true,
              message: json['message'],
              status: json['status'],
            }
          })
        }
      })
    }
  }

  handleAlertOpen = (event) => {
    this.setState({
      alert: {
        open: true,
      }
    })
  }

  handleAlertClose = (event) => {
    this.setState({
      alert: {
        open: false,
      }
    })
  };

  render() {
    const { classes } = this.props;
    const bull = <span className={classes.bullet}>•</span>;
    return (
      <Grid container spacing={2} justify='center' className={classes.root}>
        <div>
          <Snackbar open={this.state.alert.open} autoHideDuration={3000} onClose={this.handleAlertClose}>
            <MessageAlert className={classes.alert}
              serverity={this.state.alert.status}
              elevation={10}
              variant="filled"
              onClose={this.handleAlertClose} >{this.state.alert.message}</MessageAlert>
          </Snackbar>
        </div>
        <Grid item>
          <ChartCard content={this.state.memory.content} medias={(
            <LineChart name={this.state.memory.name}
              title={this.state.memory.title}
              path={this.state.memory.path}
              elements={this.state.memory.elements}
              labels={this.state.memory.labels}
              color={[randomColor(), randomColor(), randomColor()]}
              dimension={this.state.memory.dimension}       
              refreshSec={this.state.memory.refreshSec} 
              unit={this.state.memory.unit}
              />)}
            spacing={this.state.memory.spacing}
            direction={this.state.memory.direction} />
        </Grid>
        <Grid item>
          <ChartCard medias={(
            <LineChart name={this.state.cpu.name}
              title={this.state.cpu.title} 
              path={this.state.cpu.path}
              elements={this.state.cpu.elements}
              labels={this.state.cpu.labels}
              color={[randomColor(), randomColor()]}
              dimension={this.state.cpu.dimension}       
              refreshSec={this.state.cpu.refreshSec} 
              unit={this.state.cpu.unit}
              />)}
            spacing={this.state.cpu.spacing}
            direction={this.state.cpu.direction} />
        </Grid>
        <Grid item>
          <ChartCard medias={(
            <Grid container spacing={3}>
              <Grid key={0} item>
                <BarChart name={this.state.threadPool.name}
                  title={this.state.threadPool.title}
                  path={this.state.threadPool.path}
                  elements={this.state.threadPool.elements}
                  labels={this.state.threadPool.labels}
                  color={[randomColor(), randomColor(), randomColor(), randomColor()]}
                  dimension={this.state.threadPool.dimension}
                  refreshSec={this.state.threadPool.refreshSec} />
              </Grid>
              <Grid item className={classes.tabpanel}>
                <Grid item>
                <Typography className={classes.text} id='pretto-slider' gutterBottom>
                  Core Thread Size
                </Typography>
                <PrettoSlider valueLabelDisplay='auto'
                  aria-labelledby='range-slider'
                  value={this.state.corePoolSize}
                  max={this.state.maximumPoolSize}
                  onChange={(event, v) => { this.handleCorePoolSize(v) }} />
                </Grid>
                <Grid item>
                  <Typography className={classes.text} id='pretto-slider' gutterBottom>
                    Max Thread Size
                  </Typography>
                  <PrettoSlider valueLabelDisplay='auto'
                    aria-labelledby='range-slider'
                    value={this.state.maximumPoolSize}
                    max={this.state.limitPoolSize}
                    onChange={(event, v) => { this.handleMaximumPoolSize(v) }} />
                  <ApplyButton startIcon={<DoneOutlinedIcon />} onClick={(event, v) => { this.handleApply() }}> Apply </ApplyButton>
                </Grid>
              </Grid>
            </Grid>
          )}
            spacing={5}
            direction='column' />
        </Grid>
      </Grid>
    )
  }
}

class TabPanel extends Component {
  render() {
    return (
      <div hidden={this.props.value !== this.props.index}>
        <Box p={3}>{this.props.children}</Box>
      </div>
    );
  }
}

export default withStyles(dashboardThemes)(Dashboard);