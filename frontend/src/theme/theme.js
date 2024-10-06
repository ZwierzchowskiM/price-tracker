import { createTheme } from '@mui/material/styles';

const theme = createTheme({
    palette: {
        primary: {
            main: '#FFC107', // Kolor przycisku (��ty)
        },
        background: {
            default: '#FFFFFF', // Jasne t�o strony (bia�e)
        },
        text: {
            primary: '#000000', // Czarny tekst na bia�ym tle
        },
    },
    typography: {
        fontFamily: 'Roboto, sans-serif',
        h3: {
            fontWeight: 'bold',
            color: '#000000', // Czarny kolor dla nag��wk�w h3
        },
    },
});

export default theme;