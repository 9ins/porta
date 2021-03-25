import randomColor from 'randomcolor';

export const light_green = randomColor({
    luminosity: 'light',
    hue: 'green'
});
  
export const light_blue = randomColor({
    luminosity: 'light',
    hue: 'blue'
});
  
export const light_grey = randomColor({
    luminosity: 'light',
    hue: 'grey'
});
  
export const dark_green = randomColor({
    luminosity: 'dark', 
    format: 'rgba',
    hue: 'green',
    alpha: 0.5
});
   
export const dark_blue = randomColor({
    luminosity: 'dark',
    format: 'rgba',
    hue: 'blue',
    alpha: 0.5
});
  
export const dark_grey = randomColor({
    luminosity: 'dark',
    format: 'rgba',
    hue: 'grey',
    alpha: 0.5
});  
  
