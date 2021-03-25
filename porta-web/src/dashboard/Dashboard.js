import React, { Component } from 'react';
import Box from '@material-ui/core/Box';
import ChartCard from "./ChartCard";
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
  }

  componentDidMount() {
  }

  componentWillUnmount() {
  }

  render() {
    const { classes } = this.props;
    const bull = <span className={classes.bullet}>•</span>;
    return (
      <Grid container spacing={1} className={classes.root}>
        <Grid item xs>
          <ChartCard title="Resource" 
                    message="Memory Usage"  
                    medias={[(
                      <LineChart type="MEMORY"
                        element={["SystemUsed", "HeapFree", "MemoryUsed"]}
                        label={["system used", "free heap", "porta used"]}
                        stroke={[randomColor(), randomColor(), randomColor()]}            
                        unit="GB"
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
          <ChartCard title="Resource" 
                    message="CPU Usage" 
                    medias={[(
                      <LineChart type="CPU"
                        element={["CpuLoad", "SystemCpuLoad"]}
                        label={["porta load", "system load"]}
                        stroke={[randomColor(), randomColor()]}
                        unit="PCT"
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
          <ChartCard title="Resource" 
                    message="Thread Pool Usage" 
                    medias={
                      [(
                        <Grid container spacing={1}>
                          <Grid key={0} item xs={6}>
                            <BarChart type="THREAD"
                              element={["activeCount", "corePoolSize", "MaxinumPoolSize", "queueSize"]}
                              label={["active", "core", "max", "queued"]}
                              color={[randomColor(), randomColor(), randomColor(), randomColor()]}
                              unit="CNT"
                              dim={[230, 200]}
                              dist={0}
                              refreshSec={3}
                            />
                          </Grid>
                          <Grid item xs={6} className={classes.tabpanel}>
                            <Typography className={classes.text} id="pretto-slider" gutterBottom>
                              Define Core Thread
                            </Typography>
                            <PrettoSlider valueLabelDisplay="auto" aria-labelledby="range-slider" defaultValue={20} />
                            <ApplyButton startIcon={<DoneOutlinedIcon />}>Apply</ApplyButton>
                            <Grid key={2} item xs={12}>
                            <Typography className={classes.text} id="pretto-slider" gutterBottom>
                              Define Max Thread
                            </Typography>                      
                            <PrettoSlider valueLabelDisplay="auto" aria-labelledby="range-slider" defaultValue={20} />
                            <ApplyButton startIcon={<DoneOutlinedIcon />}>Apply</ApplyButton>
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