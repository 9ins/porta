import { createMuiTheme, makeStyles, withStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableContainer from '@material-ui/core/TableContainer'
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Autocomplete from '@material-ui/lab/Autocomplete';
import { Typography } from '@material-ui/core';

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
    width: '100%',
    backgroundColor: "white",
  },
  appbar: {
    width: '100%',
    backgroundColor: themeColor,
  },
  tabindicator: {
    width: '100%',
    height: '5px',
    backgroundColor: 'white',
  },
  tabpanel: {
    align: 'right',
    width: '100%',
    backgroundColor: 'white',
    backgroundColor: 'themeColor',
  },
});

export const dashboardThemes = theme => ({
  root: {
    width: '100%'
  },
  text: {
    fontSize: 14,
    fontStyle: "bold",
    color: fontWhite,
    backgroundColor: themeColor,
  },
  apply: {
    align: 'right',
    alignContent: 'right',
  },
  alert: {
    color: 'white',
    backgroundColor: themeColor,
  },
  content: {
    fontSize: 10,
  },
  title: {
    fontSize: 28,
    marginTop: 20,
  },  
  sessionInfoTitle: {
    fontSize: 20,
    fontStyle: 'bold',
    color: fontWhite,
  },
});

export const sessionsThemes = theme => ({
  root: {
    width: '100%'
  },
  header: {
    fontSize: 5,
    fontStyle: "bold",
    color: fontWhite,
    backgroundColor: themeColor,
  },
  apply: {
    align: 'right',
    alignContent: 'right',
  },
  alert: {
    color: 'white',
    backgroundColor: themeColor,
  },
  content: {
    fontSize: 8,
  },
  title: {
    fontSize: 20,
    marginTop: 20,
  },  
  sessionInfoTitle: {
    fontSize: 20,
    fontStyle: 'bold',
    color: fontWhite,
  },
  text_blue: {
    color: 'blue',
    fontStyle: 'bold',
    fontSize: 3,
  }
});

export const chartCardThemes = theme => ({
  root: {
    width: '100%',
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
    width: '100%',
    height: '100%',
    backgroundColor: themeColor,
    flexDirection: "column",
    justifyContent: "top",
  },
  cardMedia: {
    align: 'center',
    backgroundColor: themeColor,
  },
  cardcontent: {
    paddingTop: '1%',
    paddingLeft: '1%',
    fontSize: 13,
    backgroundColor: themeColor,
  },
  title: {
    fontSize: 16
  },
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
    fontFamily: [
      'Arial',
      'sans-serif',
      'Roboto',
      'BlinkMacSystemFont',
      '-apple-system',
      '"Segoe UI"',
      'Algerian',
      '"Helvetica Neue"',
      '"Apple Color Emoji"',
      '"Segoe UI Emoji"',
      '"Segoe UI Symbol"',
    ].join(','),
  },
})

export const StyledTableContainer = withStyles((theme) => ( {
  table: {

  },
}))(TableContainer)

export const StyledTextField = withStyles((theme) => ( {
  root: {
    '& label.Mui-focused' : {
      color: 'grey',
      fontSize: 12,
    },
    '& .MuiInputBase-root': {
      color: 'grey',
      fontSize: 12,
    },    
    '& .MuiInput-underline:after': {
      color: 'grey',
      borderBottomColor: 'grey',
    },    
    '& .MuiOutlinedInput-root': {
      '& fieldset': {
        borderColor: 'grey',
      },
      '&:hover fieldset': {
        borderColor: 'grey',
      },
      '&.Mui-focused fieldset': {
        borderColor: '#28b5b5',
      },
    },    
    multilineColor: 'grey',
    fontColor: 'grey',
    fontStyle: 'bold',
    width: 300,
  }
}))(TextField)

export const StyledAutocomplete = withStyles((theme) => ( {
  root: {
    '& label.Mui-focused' : {
      color: 'grey',
    },
    '& .MuiInputBase-root': {
      color: 'grey',
      fontSize: 12,
    },    
    '& .MuiInput-underline:after': {
      color: 'grey',
      fontSize: 12,
      borderBottomColor: 'grey',
    },    
    multilineColor: 'grey',
    fontColor: 'grey',
    fontSize: 10,
    fontStyle: 'bold',
    paddingTop: 0,
    width: 100,
  },
}))(Autocomplete)

export const StyledTableCell = withStyles((theme) => ({
  head: {
    backgroundColor: '#28b5b5',
    color: 'white',
    fontSize: 12,
    fontStyle: 'bold',
  },
  body: {
    color: 'grey',
    fontSize: 12,
  },
}))(TableCell);

export const StyledTableRow = withStyles((theme) => ({
  root: {
    '&:nth-of-type(odd)': {
      backgroundColor: theme.palette.action.hover,
    },
  },
}))(TableRow);

export const StyledTypography = withStyles((theme) => ({
  root: {
    fontSize: '12px',
  }  
}))(Typography);


