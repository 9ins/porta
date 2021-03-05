import React, { Component } from 'react';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import { ThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import { makeStyles } from "@material-ui/core/styles";
import { withStyles } from '@material-ui/core/styles';
import InfomationCard from "./InformationCard";
import Grid from '@material-ui/core/Grid'

import Logo from '../porta2.svg';
import LineChart from './LineChart';
import '../responsive_tabs.css';
import randomColor from 'randomcolor'


const theme = createMuiTheme({
  typography: {
    /*
    fontFamily: [
      'Algerian',
      'Roboto',
      'BlinkMacSystemFont',
      '"Segoe UI"',
      '"Helvetica Neue"',
      'Arial',
      'sans-serif',
      '"Apple Color Emoji"',
      '"Segoe UI Emoji"',
      '"Segoe UI Symbol"',
      '-apple-system',
    ].join(','),
    */
  },
});

const light_green = randomColor({
  luminosity: 'light',
  hue: 'green'
});

const light_blue = randomColor({
  luminosity: 'light',
  hue: 'blue'
});

const light_grey = randomColor({
  luminosity: 'light',
  hue: 'grey'
});

const dark_green = randomColor({
  luminosity: 'dark',
  format: 'rgba',
  hue: 'green',
  alpha: 0.5
});

const dark_blue = randomColor({
  luminosity: 'dark',
  format: 'rgba',
  hue: 'blue',
  alpha: 0.5
});

const dark_grey = randomColor({
  luminosity: 'dark',
  format: 'rgba',
  hue: 'grey',
  alpha: 0.5
});


const styles = theme => ({
  title: {
    fontSize: 16,        
    fontStyle: 'bold',
    color: '#5eaaa8',
    backgroundColor: 'white',
    paddingTop: 10, 
    paddingBottom: 10,
  },
  appbar : {
    backgroundColor: '#276678',
  },
  tabindicator: {
    height: '3px',
    backgroundColor: 'white',
  },
  tabpanel : {
    backgroundColor: 'white'
  },
  root : {
    backgroundColor: "white",
  }
});

class Dashboard extends Component {

  constructor(props) {
    super(props);
    this.state = {
      value: 0,
    }
  }

  componentDidMount() {
    this.setIntervalId = setInterval(() => {
      const requestOptions = {
          method: 'GET',
          headers: {
              'Content-Type': 'application/json',
              'username': 'admin'
          },
      };
      fetch("/resources?type=" + this.props.type + "&unit=" + this.props.unit, requestOptions)
          .then(response => response.json())
          .then(json => {
          });
    }, 3000);
  }

  handleChange = (event, newValue) => {
    this.setState({ value: newValue });
  }

  render() {
    const { classes } = this.props;
    return (
      <div className={classes.tabpanel}>
        <div className={classes.title}>
          <ThemeProvider theme={theme}>
            <Typography variant="h6" component="h5">
              <img src={Logo} width="60" height="40" />
              Porta Management Console
            </Typography>
          </ThemeProvider>
        </div>
        <div>
          <AppBar className={classes.appbar} position="static" elevation={6}>
            <Tabs value={this.state.value} onChange={this.handleChange} aria-label="simple tabs example" TabIndicatorProps={{
                className : classes.tabindicator
            }}>
              <Tab label="Dash Board" />
              <Tab label="Sessions" />
              <Tab label="Analysis" />
              <Tab label="Accounts" />
              <Tab label="Settings" />
            </Tabs>
          </AppBar>
            <TabPanel className={classes.tabpanel} value={this.state.value} index={0}>
              <Grid container spacing={2}>
                <Grid item xs>
                  <InfomationCard title="Resource" content="Memory Usage" media={(
                    <LineChart type="memory"
                      element={["SystemUsed", "HeapFree", "MemoryUsed"]}
                      stroke={[randomColor(), randomColor(), randomColor()]}
                      unit="GB"
                      dim={[400, 200]}
                      xdomain={[0, 80]}
                      ydomain={[0, 100]}
                    />)} />
                </Grid>
                <Grid item xs>
                  <InfomationCard title="Resource" content="CPU Usage" media={(
                    <LineChart type="cpu"
                      element={["CpuLoad", "SystemCpuLoad"]}
                      stroke={[randomColor(), randomColor()]}
                      unit="PCT"
                      dim={[400, 200]}
                      xdomain={[0, 80]}
                      ydomain={[0, 100]}
                    />)} />
                </Grid>
                <Grid item xs>
                  <InfomationCard title="Resource" content="Session Usage" media={(
                    <LineChart type="cpu"
                      element={["CpuLoad", "SystemCpuLoad"]}
                      stroke={[randomColor(), randomColor()]}
                      unit="PCT"
                      dim={[400, 200]}
                      xdomain={[0, 80]}
                      ydomain={[0, 100]}
                    />)} />
                </Grid>
              </Grid>
              {/**
                <Box display="flex" justifyContent="flex-end" m={2} p={3} style={{
                background: "lightgrey",
                }}>
                </Box>
              */}
            </TabPanel>

            <TabPanel value={this.state.value} index={1}>
              Sessions
            </TabPanel>

            <TabPanel value={this.state.value} index={2}>
              Analysis
            </TabPanel>

            <TabPanel value={this.state.value} index={3}>
              Accounts
            </TabPanel>

            <TabPanel value={this.state.value} index={4}>
              Settings
            </TabPanel>
        </div>
      </div>
    );
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

export default withStyles(styles)(Dashboard);