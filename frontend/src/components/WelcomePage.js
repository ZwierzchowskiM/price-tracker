import React from 'react';
import { Button, Container, Typography, Box, Grid } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const LandingPage = () => {
    const navigate = useNavigate();

    const handleStartClick = () => {
        navigate('/login'); // Przekierowanie do strony logowania
    };

    return (
        <Box
            sx={{
                backgroundColor: '#181818',
                color: '#fff',
                minHeight: '100vh',
                padding: '40px 0',
                width: '100%', // T�o na ca�� szeroko�� ekranu
            }}
        >
            <Container maxWidth="lg">
                <Grid container spacing={4} alignItems="center">
                    {/* Lewa kolumna z tekstem i przyciskiem */}
                    <Grid item xs={12} md={6}>
                        <Box sx={{ textAlign: 'left', ml: 8 }}> {/* Zwi�kszamy margines z lewej strony */}
                            <Typography variant="h2" fontWeight="bold" gutterBottom color="white">
                                Price-Tracker
                            </Typography>
                            <Box sx={{ display: 'flex', justifyContent: 'left' }}> {/* Wy�rodkowanie przycisku wzgl�dem tekstu */}
                                <Button
                                    variant="contained"
                                    color="primary"
                                    size="large"
                                    onClick={handleStartClick}
                                    sx={{ backgroundColor: '#FFC107', color: '#000', mt: 2 }}  // Dodajemy margines nad przyciskiem
                                >
                                    START
                                </Button>
                            </Box>
                        </Box>
                    </Grid>

                    {/* Prawa kolumna z galeri� obraz�w */}
                    <Grid item xs={12} md={6}>
                        <Grid container spacing={2}>
                            <Grid item xs={6}>
                                <Box
                                    sx={{
                                        height: 200,
                                        backgroundImage: `url('/images/image1.png')`,
                                        backgroundSize: 'cover',
                                        backgroundPosition: 'center',
                                        borderRadius: '16px', // Zaokr�glenie rog�w
                                    }}
                                />
                            </Grid>
                            <Grid item xs={6}>
                                <Box
                                    sx={{
                                        height: 200,
                                        backgroundImage: `url('/images/image2.png')`,
                                        backgroundSize: 'cover',
                                        backgroundPosition: 'center',
                                        borderRadius: '16px', // Zaokr�glenie rog�w
                                    }}
                                />
                            </Grid>
                            <Grid item xs={6}>
                                <Box
                                    sx={{
                                        height: 200,
                                        backgroundImage: `url('/images/image3.png')`,
                                        backgroundSize: 'cover',
                                        backgroundPosition: 'center',
                                        borderRadius: '16px', // Zaokr�glenie rog�w
                                    }}
                                />
                            </Grid>
                            <Grid item xs={6}>
                                <Box
                                    sx={{
                                        height: 200,
                                        backgroundImage: `url('/images/image4.png')`,
                                        backgroundSize: 'cover',
                                        backgroundPosition: 'center',
                                        borderRadius: '16px', // Zaokr�glenie rog�w
                                    }}
                                />
                            </Grid>
                            {/* Nowe zdj�cia */}
                            <Grid item xs={6}>
                                <Box
                                    sx={{
                                        height: 200,
                                        backgroundImage: `url('/images/image5.png')`,
                                        backgroundSize: 'cover',
                                        backgroundPosition: 'center',
                                        borderRadius: '16px', // Zaokr�glenie rog�w
                                    }}
                                />
                            </Grid>
                            <Grid item xs={6}>
                                <Box
                                    sx={{
                                        height: 200,
                                        backgroundImage: `url('/images/image6.png')`,
                                        backgroundSize: 'cover',
                                        backgroundPosition: 'center',
                                        borderRadius: '16px', // Zaokr�glenie rog�w
                                    }}
                                />
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
            </Container>
        </Box>
    );
};

export default LandingPage;
