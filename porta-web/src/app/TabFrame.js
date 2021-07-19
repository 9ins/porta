import React, { Component } from 'react';
import AppBar from '@material-ui/core/AppBar';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import { withStyles } from '@material-ui/core/styles';
import { ThemeProvider } from '@material-ui/core/styles';
import {theme, frameThemes} from './PortaThemes';
import Dashboard from '../dashboard/Dashboard';
import Sessions from '../sessions/Sessions';


import Logo from '../img/porta2.svg';

class TabFrame extends Component {

  constructor(props) {
    super(props);
    this.state = {
      value: 0,
    }
  }

  componentDidMount() {
  }

  handleChange = (event, newValue) => {
    this.setState({ value: newValue });
  }

  render() {
    const { classes } = this.props;
    const bull = <span className={classes.bullet}>•</span>;
    return (
      <div className={classes.root}>
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

          <TabPanel value={this.state.value} index={0}>
            <Dashboard></Dashboard>
          </TabPanel>              
          
          <TabPanel value={this.state.value} index={1}>
            <Sessions></Sessions>
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

  constructor(props) {
    super(props);
  }

  render() {
    const { classes } = this.props;
    return (
      <div hidden={this.props.value !== this.props.index} className={classes}>
        <Box p={2}>{this.props.children}</Box>
      </div>
    );
  }
}

export default withStyles(frameThemes)(TabFrame);