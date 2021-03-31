import { createMuiTheme, makeStyles, withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';

export const themeColor = '#007580';
export const contentBackground = '#d8ebe4';
export const buttonColor = '#007580';
export const fontColor = 'white'

export const frameThemes = theme => ({
    title: {
      fontSize: 16,        
      fontStyle: 'bold',
      color: '#5eaaa8',
      backgroundColor: 'white',
      paddingTop: 10, 
      paddingBottom: 10,
    },
    root : {
        backgroundColor: "white",
      },
    appbar : {
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
    minWidth: 300,    
  },
  text: {
    fontSize: 14,        
    fontStyle: "bold",
    color: fontColor,
    backgroundColor: themeColor,
  },
  tabpanel : {
    backgroundColor: 'white',
    paddingRight: 10,
    minWidth: 200,
    backgroundColor: themeColor,
  },
  apply: {
    align: 'right',
    alignContent: 'right',
  }
});
  
export const chartCardThemes = theme => ({
    root: {
        minWidth: 200,
        alignContent: 'center',
        padding: '10 0 0 10',
        margin: '10 10 10 10'
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
    pos: {
        marginBottom: 8,
    },
    cardcontent: {
      alignContent: 'center',
      fontSize: 18,        
      fontStyle: "bold",
      color: fontColor,
      minWidth: 100,
      paddingLeft: 20,
      paddingRight: 10,
      paddingTop: 10,
      paddingBottom: 10,
      backgroundColor: themeColor,
    }
});

export const chartThemes = theme => ({
    root: {
      backgroundColor: "white",
      border: '2px solid darkgreen',
      width: this.state.dim[0] + 30,
      height: (this.props.dim[1] + 50),
      alignContent: 'bottom'
    },
    legend: {
    },
    gridx: {
    },
    gridy: {
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
    