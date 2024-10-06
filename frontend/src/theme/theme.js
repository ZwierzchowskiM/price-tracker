import { createTheme } from '@mui/material/styles';

const theme = createTheme({
    palette: {
        primary: {
            main: '#FFC107', // Kolor przycisku (¿ó³ty)
        },
        background: {
            default: '#FFFFFF', // Jasne t³o strony (bia³e)
        },
        text: {
            primary: '#000000', // Czarny tekst na bia³ym tle
        },
    },
    typography: {
        fontFamily: 'Roboto, sans-serif',
        h3: {
            fontWeight: 'bold',
            color: '#000000', // Czarny kolor dla nag³ówków h3
        },
    },
});

export default theme;