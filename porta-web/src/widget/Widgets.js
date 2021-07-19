import { createMuiTheme, makeStyles, withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Slider from '@material-ui/core/Slider';
import MuiAlert from '@material-ui/lab/Alert';
import Typography from '@material-ui/core/Typography';
import { themeColor } from '../app/PortaThemes'

export const MessageAlert = withStyles({
  root: {
    width: '100%',
    '& > * + *': {
      marginTop: 10,
    },
  },
})(MuiAlert);

export const ApplyButton = withStyles({
  root: {
    boxShadow: 'none',
    textTransform: 'none',
    fontSize: 10,
    padding: '0px 0px',
    border: '3px solid',
    lineHeight: 1.0,
    backgroundColor: '#bbbbbb',
    borderColor: '#cccccc',
    marginTop: 0,
    marginLeft: 20,
    marginBottom: 10,
    fontFamily: [
      '-apple-system',
      'BlinkMacSystemFont',
      '"Segoe UI"',
      'Roboto',
      '"Helvetica Neue"',
      'Arial',
      'sans-serif',
      '"Apple Color Emoji"',
      '"Segoe UI Emoji"',
      '"Segoe UI Symbol"',
    ].join(','),
    '&:hover': {
      backgroundColor: '#aaaaaa',
      borderColor: '#cccccc',
      boxShadow: 'none',
    },
    '&:active': {
      /*
      boxShadow: 'none',
      backgroundColor: '#0062cc',
      borderColor: '#005cbf',
      */
    },
    '&:focus': {
      boxShadow: '0 0 0 0.2rem rgba(0,123,255,.5)',
    },
  },
})(Button);

export const PrettoSlider = withStyles({
  root: {
    color: 'white',
    height: 0,
    width: 130,
    align: 'center',
    marginRight: 0,
    marginLeft: 5
  },
  thumb: {
    height: 16,
    width: 16,
    backgroundColor: '#aaaaaa',
    border: '3px solid currentColor',
    marginTop: -4,
    marginLeft: 0,
    '&:focus, &:hover, &$active': {
      boxShadow: 'inherit',
    },
  },
  active: {},
  valueLabel: {
    color: '#b4a5a5',
    left: 'calc(-50% - 4px)',
  },
  track: {
    width: 140,
    height: 10,
    borderRadius: 4,
    border: '3px solid green',
  },
  rail: {
    width: 140,
    height: 10,
    borderRadius: 4,
    border: '3px solid green',
  },
})(Slider);


export const WhiteTextTypography = withStyles({
  root: {
    color: "#FFFFFF"
  }
})(Typography);

export const SessionNameTypography = withStyles({
  root: {
    color: themeColor,
    fontSize: 12,
    fontStyle: 'bold',
    font: 'arial'
  }
})(Typography);


