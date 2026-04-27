import React from "react";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import IconButton from "@mui/material/IconButton";
import LinearProgress from "@mui/material/LinearProgress";
import Button from "@mui/material/Button";
import { PieChart, Pie, Cell } from "recharts";
import axiosInstance from "../helper/axios";
import history from "../helper/history";
import CalendarPanel from '../components/CalendarPanel';
import dayjs from "dayjs";
const WATER_GOAL_ML = 2500;

class Home extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            user: null,
            consumed: { calories: 0, protein: 0, carbs: 0, fat: 0 },
            meals: { breakfast: 0, lunch: 0, dinner: 0, snack: 0 },
            waterMl: 0,
            weight: 0,
            selectedDate: new Date(), // creaza automat data de azi
            showCalendar: false,
        };
    }

    componentDidMount() {
        const userId = localStorage.getItem("USER_ID");
        if (userId) {
            axiosInstance.get(`/users/${userId}`)
                .then(res => this.setState({ user: res.data, weight: res.data.weight }))
                .catch(err => console.log(err));

            axiosInstance.get(`/water/${userId}/today`)
                .then(res => this.setState({ waterMl: res.data || 0 }))
                .catch(err => console.log(err));

            axiosInstance.get(`/logs/${userId}`)
                .then(res => {
                    this.setState({
                        meals: {
                            breakfast: res.data.breakfastCalories || 0,
                            lunch: res.data.lunchCalories || 0,
                            dinner: res.data.dinnerCalories || 0,
                            snack: res.data.snackCalories || 0,
                        },
                        consumed: {
                            calories: res.data.totalCalories || 0,
                            protein: res.data.totalProtein || 0,
                            carbs: res.data.totalCarbs || 0,
                            fat: res.data.totalFat || 0,
                        }
                    });
                })
                .catch(err => console.log(err));
        }
    }

    // pentru alta zi din calendar calculeaza tot
    fetchDateData = (dateStr, userId) => {
        axiosInstance.get(`/logs/${userId}?date=${dateStr}`)
            .then(res => {
                this.setState({
                    meals: {
                        breakfast: res.data.breakfastCalories || 0,
                        lunch: res.data.lunchCalories || 0,
                        dinner: res.data.dinnerCalories || 0,
                        snack: res.data.snackCalories || 0,
                    },
                    consumed: {
                        calories: res.data.totalCalories || 0,
                        protein: res.data.totalProtein || 0,
                        carbs: res.data.totalCarbs || 0,
                        fat: res.data.totalFat || 0,
                    }
                });
            })
            .catch(err => console.log(err));

        axiosInstance.get(`/water/${userId}/today?date=${dateStr}`)
            .then(res => this.setState({ waterMl: res.data || 0 }))
            .catch(err => console.log(err));

        axiosInstance.get(`/weight/${userId}/date?date=${dateStr}`)
            .then(res => {
                const val = parseFloat(res.data);
                if (!isNaN(val) && val > 0) this.setState({ weight: val });
            })
            .catch(err => console.log(err));
    };

    // schimba data cu + sau - o zi si actualizeaza noua pagina
    changeDate = (delta) => {
        const newDate = dayjs(this.state.selectedDate).add(delta, 'day').toDate();
        this.setState({ selectedDate: newDate }, () => {
            const userId = localStorage.getItem("USER_ID");
            const dateStr = dayjs(newDate).format('YYYY-MM-DD');
            this.fetchDateData(dateStr, userId);
        });
    };

    addWater = (ml) => {
        const userId = localStorage.getItem("USER_ID");
        const dateStr = this.state.selectedDate.toISOString().slice(0, 10); // taie ora

        axiosInstance.post(`/water/${userId}`, { amountMl: ml, date: dateStr })
            .then(() => axiosInstance.get(`/water/${userId}/today?date=${dateStr}`)
                .then(res => this.setState({ waterMl: Math.min(res.data, WATER_GOAL_ML) })))
            .catch(err => console.log(err));
    };

    removeWater = () => {
        const userId = localStorage.getItem("USER_ID");
        const dateStr = this.state.selectedDate.toISOString().slice(0, 10);

        axiosInstance.post(`/water/${userId}`, { amountMl: -250, date: dateStr })
            .then(() => axiosInstance.get(`/water/${userId}/today?date=${dateStr}`)
                .then(res => this.setState({ waterMl: Math.max(res.data, 0) })))
            .catch(err => console.log(err));
    };

    changeWeight = (delta) => {
        this.setState(prev => ({ weight: Math.round((prev.weight + delta) * 100) / 100 }));
    };

    saveWeight = () => {
        const userId = localStorage.getItem("USER_ID");
        const dateStr = this.state.selectedDate.toISOString().slice(0, 10);
        const weightData = { weightKg: this.state.weight };

        if (this.state.currentLogId) {
            axiosInstance.put(`/weight/${this.state.currentLogId}`, weightData)
                .then(() => alert("Greutate actualizată!"))
                .catch(e => console.error(e));
        } else {
            axiosInstance.post(`/weight/${userId}?date=${dateStr}`, weightData)
                .then(res => {
                    alert("Greutate salvată!");
                    this.setState({ currentLogId: res.data.id });
                })
                .catch(e => console.error(e));
        }
    };

    render() {
        const { user, consumed, meals, waterMl, weight, selectedDate, showCalendar } = this.state;

        const calorieGoal = user ? user.dailyCalorieGoal : 2000;
        const proteinGoal = user ? user.dailyProteinGoal : 0;
        const carbsGoal = user ? user.dailyCarbsGoal : 0;
        const fatGoal = user ? user.dailyFatGoal : 0;
        const caloriesLeft = calorieGoal - consumed.calories;
        const isOverGoal = caloriesLeft < 0;
        const overBy = Math.abs(caloriesLeft);

        const donutData = consumed.calories === 0
            ? [{ value: 0 }, { value: calorieGoal }]
            : isOverGoal
                ? [{ value: calorieGoal }, { value: 0 }]  // donut plin când depășit
                : [{ value: consumed.calories }, { value: caloriesLeft }];
        const macros = [
            { label: "Protein", consumed: consumed.protein, goal: proteinGoal, color: "#1D9E75" },
            { label: "Carbs", consumed: consumed.carbs, goal: carbsGoal, color: "#378ADD" },
            { label: "Fat", consumed: consumed.fat, goal: fatGoal, color: "#EF9F27" }
        ];

        const mealList = [
            { key: "breakfast", label: "Breakfast" },
            { key: "lunch", label: "Lunch" },
            { key: "dinner", label: "Dinner" },
            { key: "snack", label: "Snack" }
        ];

        const card = {
            borderRadius: "16px",
            border: "0.5px solid rgba(0,0,0,0.1)",
            boxShadow: "none",
            mb: 1.5
        };

        return (
            <Container maxWidth="sm" sx={{ pb: 4 }}>

                {/* Header */}
                <Card sx={{ ...card, mt: 3 }}>
                    <CardContent sx={{ p: 2, "&:last-child": { pb: 2 } }}>
                        <Typography variant="h4" fontWeight={500} sx={{ fontSize: 36 }}>
                            {user ? `Hey, ${user.name}!` : "Loading..."}
                        </Typography>
                        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mt: 1 }}>
                            <IconButton size="small" onClick={() => this.changeDate(-1)}
                                        sx={{ border: "0.5px solid rgba(0,0,0,0.15)", borderRadius: "50%", width: 32, height: 32, fontSize: 16 }}>
                                ←
                            </IconButton>
                            <Typography variant="body2" color="text.secondary">
                                {selectedDate.toLocaleDateString("en-US", {
                                    weekday: "long", month: "long", day: "numeric"
                                })}
                            </Typography>
                            <IconButton size="small" onClick={() => this.changeDate(1)}
                                        sx={{ border: "0.5px solid rgba(0,0,0,0.15)", borderRadius: "50%", width: 32, height: 32, fontSize: 16 }}>
                                →
                            </IconButton>
                        </Box>
                    </CardContent>
                </Card>

                {/* Calorii */}
                <Card sx={card}>
                    <CardContent sx={{ p: 2 }}>
                        <Typography variant="caption" color="text.secondary"
                                    sx={{ textTransform: "uppercase", letterSpacing: "0.08em", fontWeight: 500, display: "block", mb: 2 }}>
                            Daily Calories
                        </Typography>
                        <Box sx={{ position: "relative", width: 180, height: 180, mx: "auto", my: 1 }}>
                            <PieChart width={180} height={180}>
                                <Pie data={donutData} cx={85} cy={85}
                                     innerRadius={58} outerRadius={80}
                                     startAngle={90} endAngle={-270}
                                     dataKey="value" strokeWidth={0}>
                                    <Cell fill={isOverGoal ? "#E53935" : "#1D9E75"} />
                                    <Cell fill="#EEEEEE" />
                                </Pie>
                            </PieChart>
                            <Box sx={{
                                position: "absolute", top: 0, left: 0, right: 0, bottom: 0,
                                display: "flex", flexDirection: "column",
                                alignItems: "center", justifyContent: "center"
                            }}>
                                <Typography sx={{ fontSize: isOverGoal ? 22 : 28, fontWeight: 600, lineHeight: 1, color: isOverGoal ? "#E53935" : "#1D9E75" }}>
                                    {isOverGoal ? `+${overBy.toFixed(1)}` : caloriesLeft.toFixed(1)}
                                </Typography>
                                <Typography variant="caption" color="text.secondary">
                                    {isOverGoal ? "kcal over!" : "kcal left"}
                                </Typography>
                            </Box>
                        </Box>
                        <Box sx={{
                            display: "flex", flexDirection: "row",
                            justifyContent: "space-around",
                            borderTop: "0.5px solid rgba(0,0,0,0.08)",
                            pt: 2, mt: 2
                        }}>
                            {[
                                { label: "Goal", val: calorieGoal, color: "#555" },
                                { label: "Consumed", val: consumed.calories, color: "#1D9E75" },
                                { label: isOverGoal ? "Over" : "Left", val: isOverGoal ? overBy : caloriesLeft, color: isOverGoal ? "#E53935" : "#378ADD" }
                            ].map(s => (
                                <Box key={s.label} textAlign="center" flex={1}>
                                    <Typography sx={{ fontSize: 20, fontWeight: 600, color: s.color }}>
                                        {parseFloat(s.val).toFixed(1)}
                                    </Typography>
                                    <Typography variant="caption" color="text.secondary">{s.label}</Typography>
                                </Box>
                            ))}
                        </Box>
                    </CardContent>
                </Card>


                {/* Macros */}
                <Card sx={card}>
                    <CardContent sx={{ p: 2 }}>
                        <Typography variant="caption" color="text.secondary"
                                    sx={{ textTransform: "uppercase", letterSpacing: "0.08em", fontWeight: 500, display: "block", mb: 2 }}>
                            Macronutrients
                        </Typography>
                        <Box display="flex" gap={2}>
                            {macros.map(macro => (
                                <Box key={macro.label} flex={1}>
                                    <Typography variant="caption" color="text.secondary" display="block" mb={0.5}>
                                        {macro.label}
                                    </Typography>
                                    <LinearProgress variant="determinate"
                                                    value={macro.goal > 0 ? Math.min((macro.consumed / macro.goal) * 100, 100) : 0}
                                                    sx={{
                                                        height: 8, borderRadius: 4,
                                                        backgroundColor: "#EEEEEE",
                                                        "& .MuiLinearProgress-bar": { backgroundColor: macro.color, borderRadius: 4 }
                                                    }} />
                                    <Typography variant="caption" color="text.secondary" display="block" mt={0.5}>
                                        {macro.consumed} / {macro.goal}g
                                    </Typography>
                                </Box>
                            ))}
                        </Box>
                    </CardContent>
                </Card>

                {/* Mese */}
                <Typography variant="caption" color="text.secondary"
                            sx={{ textTransform: "uppercase", letterSpacing: "0.08em", fontWeight: 500, display: "block", mb: 1, mt: 1 }}>
                    Today's Meals
                </Typography>
                {mealList.map(meal => (
                    <Card key={meal.key} sx={{ ...card, cursor: "pointer" }}
                          onClick={() => {
                              const dateStr = this.state.selectedDate.toISOString().slice(0, 10);
                              history.push(`/meal-detail?meal=${meal.key.toUpperCase()}&date=${dateStr}`);
                              window.location.reload();
                          }}>
                        <CardContent sx={{ py: "12px !important", px: 2 }}>
                            <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                <Box>
                                    <Typography fontWeight={500} fontSize={15}>{meal.label}</Typography>
                                    <Typography variant="caption" sx={{ color: "#1D9E75", fontWeight: 500 }}>
                                        {parseFloat(meals[meal.key]).toFixed(1)} kcal
                                    </Typography>
                                </Box>
                                <IconButton size="small"
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                const dateStr = this.state.selectedDate.toISOString().slice(0, 10);
                                                history.push(`/add-food?meal=${meal.key.toUpperCase()}&date=${dateStr}`);
                                                window.location.reload();
                                            }}
                                            sx={{ border: "0.5px solid rgba(0,0,0,0.15)", borderRadius: "50%", width: 32, height: 32 }}>
                                    <span style={{ fontSize: 18, lineHeight: 1 }}>+</span>
                                </IconButton>
                            </Box>
                        </CardContent>
                    </Card>
                ))}

                {/* Water */}
                <Card sx={{ ...card, mt: 1.5 }}>
                    <CardContent sx={{ p: 2 }}>
                        <Typography variant="caption" color="text.secondary"
                                    sx={{ textTransform: "uppercase", letterSpacing: "0.08em", fontWeight: 500, display: "block", mb: 2 }}>
                            Water Tracker
                        </Typography>
                        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "flex-end", mb: 2 }}>
                            <Box>
                                <Typography sx={{ fontSize: 28, fontWeight: 600, lineHeight: 1, color: "#000000" }}>
                                    {(waterMl / 1000).toFixed(2)}L
                                </Typography>
                                <Typography variant="caption" color="text.secondary">
                                    of {(WATER_GOAL_ML / 1000).toFixed(1)}L goal
                                </Typography>
                            </Box>
                            <Typography variant="caption" color="text.secondary">
                                {Math.round((waterMl / WATER_GOAL_ML) * 100)}%
                            </Typography>
                        </Box>
                        <Box sx={{ display: "flex", flexDirection: "row", gap: 1, width: "100%" }}>
                            <Button variant="outlined" size="small"
                                    onClick={() => this.addWater(250)}
                                    sx={{ borderRadius: 2, textTransform: "none", fontSize: 12, flex: 1, minWidth: 0 }}>
                                + 250ml
                            </Button>
                            <Button variant="outlined" size="small"
                                    onClick={() => this.addWater(500)}
                                    sx={{ borderRadius: 2, textTransform: "none", fontSize: 12, flex: 1, minWidth: 0 }}>
                                + 500ml
                            </Button>
                            <Button variant="outlined" size="small"
                                    onClick={this.removeWater}
                                    sx={{ borderRadius: 2, textTransform: "none", fontSize: 12, flex: 1, minWidth: 0 }}>
                                − Remove
                            </Button>
                        </Box>
                    </CardContent>
                </Card>

                {/* Measurement */}
                <Card sx={card}>
                    <CardContent sx={{ p: 2 }}>
                        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                            <Typography variant="caption" color="text.secondary"
                                        sx={{ textTransform: "uppercase", letterSpacing: "0.08em", fontWeight: 500 }}>
                                Measurement
                            </Typography>

                        </Box>
                        <Box sx={{ display: "flex", flexDirection: "row", alignItems: "center", justifyContent: "center", gap: 3 }}>
                            <IconButton onClick={() => this.changeWeight(-0.1)}
                                        sx={{ width: 44, height: 44, border: "0.5px solid rgba(0,0,0,0.15)", borderRadius: "50%", fontSize: 22 }}>
                                −
                            </IconButton>
                            <Box textAlign="center">
                                <Typography sx={{ fontSize: 44, fontWeight: 600, lineHeight: 1, color: "black" }}>
                                    {weight.toFixed(2)} kg
                                </Typography>
                                <Typography variant="caption" color="text.secondary">kg</Typography>
                            </Box>
                            <IconButton onClick={() => this.changeWeight(0.1)}
                                        sx={{ width: 44, height: 44, border: "0.5px solid rgba(0,0,0,0.15)", borderRadius: "50%", fontSize: 22 }}>
                                +
                            </IconButton>
                        </Box>
                        <Box sx={{ display: "flex", gap: 1, mt: 2 }}>
                            <Button variant="outlined" onClick={this.saveWeight}
                                    sx={{ borderRadius: 2, textTransform: "none", fontSize: 13, flex: 1, minWidth: 0 }}>
                                Save measurement
                            </Button>
                            <Button variant="outlined"
                                    onClick={() => { history.push("/weight-progress"); window.location.reload(); }}
                                    sx={{ borderRadius: 2, textTransform: "none", fontSize: 13, flex: 1, minWidth: 0 }}>
                                View Progress
                            </Button>
                        </Box>
                    </CardContent>
                </Card>

                {/* Button calendar */}
                <Card sx={{ ...card, mt: 3 }}>
                    <CardContent sx={{ p: 2, "&:last-child": { pb: 2 } }}>
                        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
                            <Typography variant="caption" color="text.secondary"
                                        sx={{ textTransform: "uppercase", letterSpacing: "0.08em", fontWeight: 500 }}>
                                Calendar
                            </Typography>
                        </Box>
                        <Button fullWidth size="small" variant="outlined"
                                onClick={() => this.setState(prev => ({ showCalendar: !prev.showCalendar }))}
                                sx={{ mt: 1.5, borderRadius: 2, textTransform: "none", fontSize: 12 }}>
                            {showCalendar ? "Hide Calendar" : "Show Calendar"}
                        </Button>
                    </CardContent>
                </Card>

                {/* Calendar */}
                {showCalendar && (
                    <CalendarPanel
                        selectedDate={selectedDate}
                        calorieGoal={calorieGoal}
                        onDateSelect={(dateStr, year, month, day) => {
                            const newDate = new Date(year, month, day);
                            const userId = localStorage.getItem("USER_ID");
                            this.setState({ selectedDate: newDate });
                            this.fetchDateData(dateStr, userId);
                        }}
                    />
                )}


            </Container>
        );
    }
}

export default Home;