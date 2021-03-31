import React, { Component } from 'react';
import Box from '@material-ui/core/Box';
import ChartCard from './ChartCard';
import Grid from '@material-ui/core/Grid';
import LineChart from './LineChart';
import BarChart from './BarChart';
import randomColor from 'randomcolor';
import { withStyles } from '@material-ui/core/styles';
import { chartCardThemes, dashboardThemes } from '../app/PortaThemes.js';
import { PrettoSlider, ApplyButton} from '../widget/Widgets';
import DoneOutlinedIcon from '@material-ui/icons/DoneOutlined';
import Typography from '@material-ui/core/Typography';
import FetchRequest from '../app/FetchRequest';

import '../css/responsive_tabs.css';

class Dashboard extends Component {

  constructor(props) {
    super(props);
    this.state = {
      path: '/resources',
      type: 'THREAD',
      unit: "CNT",
      corePoolSize : 0,
      maximumPoolSize : 0,
      queueSize: 0,
    }
    this.request = new FetchRequest();
  }

  componentDidMount() {
    this.request.fetchGet(this.state.path, this.state.type, this.state.unit)
    .then(json => {            
      console.log(json);
      this.setState ({
        corePoolSize: json['corePoolSize'],
        maximumPoolSize: json['maxinumPoolSize'],
      });
    })
  }  

  componentWillUnmount() {
    clearInterval(this.setIntervalId);
  }  

  handleCorePoolSize(v) {
    this.setState({
      corePoolSize: v,
    });
  }

  handleMaximumPoolSize(v) {
    this.setState({
      maximumPoolSize: v,
    })
  }

  handleApply() {
    const body = {
      corePoolSize : this.state.corePoolSize,
      maximumPoolSize : this.state.maximumPoolSize,
    }
    this.request.fetchPostRequest(this.state.path, "THREAD_POOL", body);
  }

  render() {
    const { classes } = this.props;
    const bull = <span className={classes.bullet}>•</span>;
    return (
      <Grid container spacing={1} className={classes.root}>
        <Grid item xs>
          <ChartCard title='Resource' 
                    message='Memory Usage'  
                    medias={[(
                      <LineChart 
                        method='get'
                        path='/resources'
                        type='MEMORY'
                        element={['systemUsed', 'heapFree', 'memoryUsed']}
                        label={['system', 'heapFree', 'memory']}
                        stroke={[randomColor(), randomColor(), randomColor()]}
                        unit='GB'
                        dim={[460, 200]}
                        xdomain={[0, 80]}
                        ydomain={[0, 100]}
                        refreshSec={3}
                      />)]}
                    spacing={5}
                    direction='column'
          />
        </Grid>
        <Grid item xs>
          <ChartCard title='Resource' 
                    message='CPU Usage' 
                    medias={[(
                      <LineChart 
                        method='get'
                        path='/resources'
                        type='CPU'
                        unit='PCT'
                        element={['cpuLoad', 'systemCpuLoad']}
                        label={['cpu', 'systemCpu']}
                        stroke={[randomColor(), randomColor()]}
                        dim={[460, 200]}
                        xdomain={[0, 80]}
                        ydomain={[0, 100]}
                        refreshSec={3}
                      />)]} 
                      spacing={5}
                      direction='column'
          />
        </Grid>
        <Grid item xs>
          <ChartCard title='Resource' 
                    message='Thread Pool Usage' 
                    medias={
                      [(
                        <Grid container spacing={1}>
                          <Grid key={0} item xs={6}>
                            <BarChart 
                              method='get'
                              path='/resources'
                              type='THREAD'
                              unit='CNT'
                              element={['activeCount', 'corePoolSize', 'maxinumPoolSize', 'queueSize']}
                              label={['active', 'core', 'max', 'queued']}
                              color={[randomColor(), randomColor(), randomColor(), randomColor()]}
                              dim={[230, 200]}
                              dist={0}
                              refreshSec={3}
                            />
                          </Grid>
                          <Grid key={1} item xs={6} className={classes.tabpanel}>
                            <Typography className={classes.text} id='pretto-slider' gutterBottom>
                              Core Thread Size
                            </Typography>
                            <PrettoSlider valueLabelDisplay='auto' aria-labelledby='range-slider' value={this.state.corePoolSize} onChange={(event, v) => {
                              this.handleCorePoolSize(v);
                            }} />
                            <Grid key={3} item xs={12}>
                              <Typography className={classes.text} id='pretto-slider' gutterBottom>
                                Max Thread Size
                              </Typography>
                              <PrettoSlider valueLabelDisplay='auto' aria-labelledby='range-slider' value={this.state.maximumPoolSize} onChange={(event, v) => {
                                this.handleMaximumPoolSize(v);
                              }} />
                              <ApplyButton startIcon={<DoneOutlinedIcon />} onClick={(event, v) => {
                                this.handleApply()
                              }} >Apply</ApplyButton>
                            </Grid>
                          </Grid>
                      </Grid>
                    )]}
                    spacing={5}
                    direction='column'/>
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