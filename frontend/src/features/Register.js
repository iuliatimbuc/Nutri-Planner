import React from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Container from "@mui/material/Container";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import MenuItem from "@mui/material/MenuItem";
import LinearProgress from "@mui/material/LinearProgress";
import axiosInstance from "../helper/axios";
import history from "../helper/history";
import dayjs from "dayjs";

const steps = [
    "Name", "Age & Gender", "Height", "Current Weight",
    "Target Weight", "Goal", "Activity Level", "Target Date", "Account"
];

class Register extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            step: 0,
            name: "",
            age: "",
            gender: "",
            height: "",
            weight: "",
            targetWeight: "",
            goal: "",
            activityLevel: "",
            targetDate: "",
            email: "",
            password: "",
            confirmPassword: "",
            error: ""
        };
    }

    handleInput = event => {
        const { value, name } = event.target;
        this.setState({ [name]: value, error: "" });
    };

    getAvailableGoals = () => {
        const current = parseFloat(this.state.weight);
        const target = parseFloat(this.state.targetWeight);
        if (isNaN(current) || isNaN(target)) return [];
        if (target > current) return [
            { value: "GAIN_MUSCLE", label: "Gain Muscle" },
            { value: "GAIN_WEIGHT", label: "Gain Weight" }
        ];
        if (target < current) return [
            { value: "GAIN_MUSCLE", label: "Gain Muscle" },
            { value: "LOSE_WEIGHT", label: "Lose Weight" }
        ];
        return [{ value: "MAINTAIN", label: "Maintain" }];
    };

    // functie de validare pentru fiecare data introdusa
    validateStep = () => {
        const { step, name, age, gender, height, weight, targetWeight,
            goal, activityLevel, targetDate, email, password, confirmPassword } = this.state;

        switch (step) {
            case 0:
                if (!name.trim()) { this.setState({ error: "Please enter your name." }); return false; }
                break;
            case 1:
                if (!age || age <= 0 || age>100) { this.setState({ error: "Please enter a valid age." }); return false; }
                if (!gender) { this.setState({ error: "Please select your gender." }); return false; }
                break;
            case 2:
                if (!height || height <= 0 || height > 200) { this.setState({ error: "Please enter a valid height." }); return false; }
                break;
            case 3:
                if (!weight || weight <= 0 || weight > 500) { this.setState({ error: "Please enter a valid weight." }); return false; }
                break;
            case 4:
                if (!targetWeight || targetWeight <= 0 || targetWeight > 500) { this.setState({ error: "Please enter a valid target weight." }); return false; }
                break;
            case 5:
                if (!goal) { this.setState({ error: "Please select a goal." }); return false; }
                break;
            case 6:
                if (!activityLevel) { this.setState({ error: "Please select your activity level." }); return false; }
                break;
            case 7:
                if (!targetDate) { this.setState({ error: "Please select a target date." }); return false; }
                const minDate = dayjs().add(14, 'day');
                if (dayjs(targetDate).isBefore(minDate)) {
                    this.setState({ error: "Target date must be at least 2 weeks from today." });
                    return false;
                }
                break;
            case 8:
                if (!email.trim()) { this.setState({ error: "Please enter your email." }); return false; }
                if (!password) { this.setState({ error: "Please enter a password." }); return false; }
                if (password.length < 6) { this.setState({ error: "Password must be at least 6 characters." }); return false; }
                if (password !== confirmPassword) { this.setState({ error: "Passwords do not match." }); return false; }
                break;
            default:
                break;
        }
        return true;
    };

    nextStep = () => {
        if (this.validateStep()) {
            // auto-set goal daca e aceeasi greutate ca sa pot da next
            if (this.state.step === 4) {
                const current = parseFloat(this.state.weight);
                const target = parseFloat(this.state.targetWeight);
                if (current === target) {
                    this.setState({ step: this.state.step + 1, goal: "MAINTAIN" });
                    return;
                }
            }
            this.setState({ step: this.state.step + 1 , error: "" });
        }
    };

    prevStep = () => {
        this.setState({ step: this.state.step - 1, error: "" });
    };

    onSubmit = event => {
        if (event) event.preventDefault(); // opreste comportamentul default
        if (!this.validateStep()) return;

        const { name, age, gender, height, weight, targetWeight,
            goal, activityLevel, targetDate, email, password } = this.state;

        const userData = {
            name, email, password,
            age: parseInt(age),
            gender,
            weight: parseFloat(weight),
            height: parseFloat(height),
            targetWeight: parseFloat(targetWeight),
            targetDate,
            goal,
            activityLevel
        };

        axiosInstance.post("/users", userData)
            .then(() => {
                alert("Account created successfully!");
                history.push("/login");
                window.location.reload();
            })
            .catch(error => {
                console.log(error);
                this.setState({ error: "Registration failed. Please try again." });
            });
    };

    renderStep = () => {
        const { step, name, age, gender, height, weight, targetWeight,
            goal, activityLevel, targetDate, email, password, confirmPassword, error } = this.state;

        const errorBox = error ? (
            <Typography color="error" variant="body2" mt={1}>{error}</Typography>
        ) : null;

        switch (step) {
            case 0:
                return (
                    <Box>
                        <Typography variant="h6" gutterBottom>What's your name?</Typography>
                        <TextField fullWidth variant="outlined" margin="normal"
                                   label="Full Name" name="name" value={name} onChange={this.handleInput} />
                        {errorBox}
                    </Box>
                );
            case 1:
                return (
                    <Box>
                        <Typography variant="h6" gutterBottom>How old are you and what's your gender?</Typography>
                        <TextField fullWidth variant="outlined" margin="normal"
                                   label="Age" name="age" type="number" value={age} onChange={this.handleInput} />
                        <TextField select fullWidth variant="outlined" margin="normal"
                                   label="Gender" name="gender" value={gender} onChange={this.handleInput}>
                            <MenuItem value="MALE">Male</MenuItem>
                            <MenuItem value="FEMALE">Female</MenuItem>
                        </TextField>
                        {errorBox}
                    </Box>
                );
            case 2:
                return (
                    <Box>
                        <Typography variant="h6" gutterBottom>How tall are you?</Typography>
                        <TextField fullWidth variant="outlined" margin="normal"
                                   label="Height (cm)" name="height" type="number" value={height} onChange={this.handleInput} />
                        {errorBox}
                    </Box>
                );
            case 3:
                return (
                    <Box>
                        <Typography variant="h6" gutterBottom>How much do you weigh currently?</Typography>
                        <TextField fullWidth variant="outlined" margin="normal"
                                   label="Current Weight (kg)" name="weight" type="number" value={weight} onChange={this.handleInput} />
                        {errorBox}
                    </Box>
                );
            case 4:
                return (
                    <Box>
                        <Typography variant="h6" gutterBottom>What's your target weight?</Typography>
                        <TextField fullWidth variant="outlined" margin="normal"
                                   label="Target Weight (kg)" name="targetWeight" type="number" value={targetWeight} onChange={this.handleInput} />
                        {errorBox}
                    </Box>
                );
            case 5:
                const goals = this.getAvailableGoals();
                if (goals.length === 1 && goals[0].value === "MAINTAIN") {
                    return (
                        <Box>
                            <Typography variant="h6" gutterBottom>Your goal</Typography>
                            <Typography variant="body1" color="text.secondary">
                                Since your current and target weight are the same, your goal is automatically set to <strong>Maintain</strong>.
                            </Typography>
                        </Box>
                    );
                }
                return (
                    <Box>
                        <Typography variant="h6" gutterBottom>What's your goal?</Typography>
                        <TextField select fullWidth variant="outlined" margin="normal"
                                   label="Goal" name="goal" value={goal} onChange={this.handleInput}>
                            {goals.map(g => (
                                <MenuItem key={g.value} value={g.value}>{g.label}</MenuItem>
                            ))}
                        </TextField>
                        {errorBox}
                    </Box>
                );
            case 6:
                return (
                    <Box>
                        <Typography variant="h6" gutterBottom>How active are you?</Typography>
                        <TextField select fullWidth variant="outlined" margin="normal"
                                   label="Activity Level" name="activityLevel" value={activityLevel} onChange={this.handleInput}>
                            <MenuItem value="SEDENTARY">Sedentary (little or no exercise)</MenuItem>
                            <MenuItem value="LIGHTLY_ACTIVE">Lightly Active (1-3 days/week)</MenuItem>
                            <MenuItem value="MODERATELY_ACTIVE">Moderately Active (3-5 days/week)</MenuItem>
                            <MenuItem value="VERY_ACTIVE">Very Active (6-7 days/week)</MenuItem>
                            <MenuItem value="EXTRA_ACTIVE">Extra Active (very hard exercise)</MenuItem>
                        </TextField>
                        {errorBox}
                    </Box>
                );
            case 7:
                const minDate = dayjs().add(14, 'day').format('YYYY-MM-DD');
                return (
                    <Box>
                        <Typography variant="h6" gutterBottom>When do you want to reach your goal?</Typography>
                        <TextField fullWidth variant="outlined" margin="normal"
                                   name="targetDate" type="date"
                                   value={targetDate} onChange={this.handleInput}
                                   inputProps={{ min: minDate }}
                                   InputLabelProps={{ shrink: true }} />
                        {errorBox}
                    </Box>
                );
            case 8:
                return (
                    <Box>
                        <Typography variant="h6" gutterBottom>Create your account</Typography>
                        <TextField fullWidth variant="outlined" margin="normal"
                                   label="Email" name="email" type="email" value={email} onChange={this.handleInput} />
                        <TextField fullWidth variant="outlined" margin="normal"
                                   label="Password" name="password" type="password" value={password} onChange={this.handleInput} />
                        <TextField fullWidth variant="outlined" margin="normal"
                                   label="Confirm Password" name="confirmPassword" type="password"
                                   value={confirmPassword} onChange={this.handleInput} />
                        {errorBox}
                    </Box>
                );
            default:
                return null;
        }
    };

    render() {
        const { step } = this.state;
        const progress = (step / (steps.length - 1)) * 100;

        return (
            <Container maxWidth="sm">
                <Box mt={8} mb={4}>
                    <Typography variant="h4" align="center" gutterBottom>
                        Nutri Planner
                    </Typography>

                    <Card elevation={3}>
                        <CardContent>
                            <Box mb={2}>
                                <LinearProgress variant="determinate" value={progress} />
                                <Typography variant="caption" color="text.secondary">
                                    Step {step + 1} of {steps.length}
                                </Typography>
                            </Box>

                            {this.renderStep()}

                            <Box mt={3} display="flex" justifyContent="space-between">
                                {step > 0 && (
                                    <Button variant="outlined" onClick={this.prevStep}>
                                        Back
                                    </Button>
                                )}
                                {step < steps.length - 1 ? (
                                    <Button variant="contained" color="primary"
                                            onClick={this.nextStep} sx={{ ml: "auto" }}>
                                        Next
                                    </Button>
                                ) : (
                                    <Button variant="contained" color="primary"
                                            onClick={this.onSubmit} sx={{ ml: "auto" }}>
                                        Create Account
                                    </Button>
                                )}
                            </Box>

                            <Box mt={2}>
                                <Button fullWidth variant="text" color="secondary"
                                        onClick={() => { history.push("/login"); window.location.reload(); }}>
                                    Already have an account? Sign In
                                </Button>
                            </Box>
                        </CardContent>
                    </Card>
                </Box>
            </Container>
        );
    }
}

export default Register;