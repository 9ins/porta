import React, { Component } from 'react';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import { ThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import { makeStyles } from "@material-ui/core/styles";
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


const useStyles = makeStyles({
  root: {
    boxShadow: "none",
    backgroundColor: "#cccccc"
  }
});

class Dashboard extends Component {

  constructor(props) {
    super(props);
    this.state = {
      value: 0
    }
  }

  handleChange = (event, newValue) => {
    this.setState({ value: newValue });
  }

  render() {
    return (
      <div>
        <div className="main" style={{ backgroundColor: "white", color: "#5eaaa8", paddingTop: 10, paddingBottom: 10 }}>
          <ThemeProvider theme={theme}>
            <Typography variant="h6" component="h5">
              <img src={Logo} width="60" height="40" />
          Porta Management Console
        </Typography>
          </ThemeProvider>
        </div>
        <div>
          <AppBar position="static" elevation={6} style={{ backgroundColor: '#276678' }}>
            <Tabs value={this.state.value} onChange={this.handleChange} aria-label="simple tabs example" TabIndicatorProps={{
              style: {
                height: "6px",
                backgroundColor: "#98ded9",
              }
            }}>
              <Tab label="Dash Board" />
              <Tab label="Sessions" />
              <Tab label="Analysis" />
              <Tab label="Accounts" />
              <Tab label="Settings" />
            </Tabs>
          </AppBar>
          <Box>
            <TabPanel value={this.state.value} index={0}>
              <Grid container spacing={3}>
                <Grid item xs> 
                  <InfomationCard title="Resource" content="Memory Resource" media={(
                      <LineChart type="memory" 
                                  element={["usedHeapMemory", "maxHeapMemory"]} 
                                  stroke={[randomColor(), randomColor()]} 
                                  unit="GB" 
                                  width={400} 
                                  height={200} 
                                  domain={[0, 64]}
                                  />)} />
                </Grid>
                <Grid item xs>
                  <InfomationCard title="Resource" content="CPU Resource" media={(
                      <LineChart type="cpu" 
                                  element={["processCpuLoad", "systemCpuLoad"]} 
                                  stroke={[randomColor(), randomColor()]} 
                                  unit="PCT" 
                                  width={400} 
                                  height={200} 
                                  domain={[0, 100]}
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
          </Box>
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

export default Dashboard;
