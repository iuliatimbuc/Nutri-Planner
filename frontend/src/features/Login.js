import React from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Container from "@mui/material/Container";
import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import axiosInstance from "../helper/axios";
import history from '../helper/history';

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            email: "",
            password: "",
            loginSuccess: {
                id: 0,
            }
        };
    }

    handleInput = event => {
        const { value, name } = event.target;
        this.setState({ [name]: value });
    };

    onSubmitFunction = event => {
        event.preventDefault();
        let credentials = {
            email: this.state.email,
            password: this.state.password
        };

        axiosInstance.post("/auth/login", credentials)
            .then(res => {
                const val = res.data;
                this.setState({ loginSuccess: val });
                if (val.id !== 0) {
                    localStorage.setItem("USER_ID", res.data.id);
                    history.push("/home");
                    window.location.reload();
                }
            })
            .catch(error => {
                console.log(error);
                alert("Invalid Credentials");
            });
    };

    render() {
        return (
            <Container maxWidth="sm">
                <Box mt={8}>
                    <Typography variant="h4" align="center" gutterBottom>
                        Nutri Planner
                    </Typography>

                    <Card elevation={3}>
                        <CardContent>
                            <Grid container>
                                <form onSubmit={this.onSubmitFunction} style={{ width: "100%" }}>
                                    <TextField
                                        variant="outlined"
                                        margin="normal"
                                        fullWidth
                                        id="email"
                                        label="Email"
                                        name="email"
                                        type="email"
                                        onChange={this.handleInput}
                                    />
                                    <TextField
                                        variant="outlined"
                                        margin="normal"
                                        fullWidth
                                        name="password"
                                        label="Password"
                                        type="password"
                                        id="password"
                                        onChange={this.handleInput}
                                    />
                                    <Box mt={2}>
                                        <Button
                                            type="submit"
                                            fullWidth
                                            variant="contained"
                                            color="primary"
                                        >
                                            Sign In
                                        </Button>
                                    </Box>
                                    <Box mt={1}>
                                        <Button
                                            fullWidth
                                            variant="text"
                                            color="secondary"
                                            onClick={() => {
                                                history.push("/register");
                                                window.location.reload();
                                            }}
                                        >
                                            Don't have an account? Register
                                        </Button>
                                    </Box>
                                </form>
                            </Grid>
                        </CardContent>
                    </Card>
                </Box>
            </Container>
        );
    }
}

export default Login;