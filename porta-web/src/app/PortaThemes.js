import { createMuiTheme, makeStyles, withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';

export const themeColor = '#007580';
export const contentBackground = '#d8ebe4';
export const buttonColor = '#007580';
export const fontColor = '#777777';
export const fontWhite = '#ffffff';
export const chartWidth = window.innerWidth * 0.3;
export const chartHeight = window.innerHeight * 0.23;
export const cardWidth = window.innerWidth * 0.3;
export const cardHeight = window.innerHeight * 0.23;

export const frameThemes = theme => ({
  title: {
    fontSize: 16,
    fontStyle: 'bold',
    color: themeColor,
    backgroundColor: 'white',
    paddingTop: 10,
    paddingBottom: 10,
  },
  root: {
    backgroundColor: "white",
  },
  appbar: {
    backgroundColor: themeColor,
    paddingRight: 10,
  },
  tabindicator: {
    height: '5px',
    backgroundColor: 'white',
  },
});

export const dashboardThemes = theme => ({
  root: {
  },
  text: {
    fontSize: 14,
    fontStyle: "bold",
    color: fontWhite,
    backgroundColor: themeColor,
  },
  tabpanel: {
    backgroundColor: 'white',
    paddingRight: 10,
    backgroundColor: 'themeColor',
  },
  apply: {
    align: 'right',
    alignContent: 'right',
  },
  alert: {
    color: 'white',
    backgroundColor: themeColor,
  }
});

export const chartCardThemes = theme => ({
  root: {
    alignContent: 'right',
  },
  button: {
    fontSize: 12,
    fontStyle: "bold",
    color: fontColor,
    backgroundColor: themeColor,
  },
  bullet: {
    display: 'inline-block',
    margin: '0 1px',
    transform: 'scale(1.8)',
  },
  paper: {
    height: '100%',
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    //backgroundColor: themeColor,
  },
  cardcontent: {
    width: '100%',
    height: '100%',
    align: 'center',
    fontSize: 18,
    color: 'white',
    backgroundColor: themeColor,
  }
});

export const chartThemes = theme => ({
  root: {
    width: '100%',
    height: '100%',
    alignContent: 'bottom',
    border: '2px solid darkgreen',
    backgroundColor: "#fefefe",
  },
  bullet: {
    display: 'inline-block',
    transform: 'scale(1.8)',
  },
  title: {
    fontSize: 18,
    color: fontColor,
  },
  content: {
    fontSize: 10,
    fontColor: '#111111',
  },
  legend: {
  },
  xy: {
    stroke: fontColor,
  }
})

export const theme = createMuiTheme({
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
