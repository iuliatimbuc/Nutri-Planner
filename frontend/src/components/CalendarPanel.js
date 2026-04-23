import React from "react";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import IconButton from "@mui/material/IconButton";
import axiosInstance from "../helper/axios";

class CalendarPanel extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            calendarMonth: new Date(),
            monthlySummary: {},
        };
    }

    componentDidMount() {
        this.fetchMonthlySummary(new Date());
    }

    fetchMonthlySummary = (date) => {
        const userId = localStorage.getItem("USER_ID");
        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        axiosInstance.get(`/logs/${userId}/monthly-summary?year=${year}&month=${month}`)
            .then(res => this.setState({ monthlySummary: res.data }))
            .catch(err => console.log(err));
    };

    changeCalendarMonth = (delta) => {
        this.setState(prev => {
            const newMonth = new Date(prev.calendarMonth);
            newMonth.setMonth(newMonth.getMonth() + delta);
            return { calendarMonth: newMonth };
        }, () => this.fetchMonthlySummary(this.state.calendarMonth));
    };

    render() {
        const { calendarMonth, monthlySummary } = this.state;
        const { selectedDate, calorieGoal, onDateSelect } = this.props;

        const year = calendarMonth.getFullYear();
        const month = calendarMonth.getMonth();
        const firstDay = new Date(year, month, 1).getDay();
        const daysInMonth = new Date(year, month + 1, 0).getDate();
        const blanks = Array(firstDay).fill(null);
        const days = Array.from({ length: daysInMonth }, (_, i) => i + 1);
        const allCells = [...blanks, ...days];

        const selectedStr = selectedDate?.toISOString().slice(0, 10);
        const todayStr = new Date().toISOString().slice(0, 10);

        return (
            <Card sx={{ borderRadius: "16px", border: "0.5px solid rgba(0,0,0,0.1)", boxShadow: "none", mb: 1.5 }}>
                <CardContent sx={{ p: 2 }}>

                    {/* Header luna */}
                    <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 1.5 }}>
                        <IconButton size="small" onClick={() => this.changeCalendarMonth(-1)}
                                    sx={{ border: "0.5px solid rgba(0,0,0,0.15)", borderRadius: "50%", width: 28, height: 28, fontSize: 14 }}>
                            ←
                        </IconButton>
                        <Typography variant="caption" color="text.secondary"
                                    sx={{ textTransform: "uppercase", letterSpacing: "0.08em", fontWeight: 500 }}>
                            {calendarMonth.toLocaleDateString("en-US", { month: "long", year: "numeric" })}
                        </Typography>
                        <IconButton size="small" onClick={() => this.changeCalendarMonth(1)}
                                    sx={{ border: "0.5px solid rgba(0,0,0,0.15)", borderRadius: "50%", width: 28, height: 28, fontSize: 14 }}>
                            →
                        </IconButton>
                    </Box>

                    {/* Zilele saptamanii */}
                    <Box sx={{ display: "grid", gridTemplateColumns: "repeat(7, 1fr)", mb: 0.5 }}>
                        {["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"].map(d => (
                            <Typography key={d} variant="caption" color="text.secondary"
                                        sx={{ textAlign: "center", fontWeight: 500, fontSize: 10 }}>
                                {d}
                            </Typography>
                        ))}
                    </Box>

                    {/* Zilele lunii */}
                    <Box sx={{ display: "grid", gridTemplateColumns: "repeat(7, 1fr)", gap: 0.3 }}>
                        {allCells.map((day, i) => {
                            if (!day) return <Box key={`blank-${i}`} />;

                            const dateStr = `${year}-${String(month + 1).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
                            const calories = monthlySummary[dateStr];
                            const isToday = todayStr === dateStr;
                            const isSelected = selectedStr === dateStr;

                            return (
                                <Box key={day}
                                     onClick={() => onDateSelect && onDateSelect(dateStr, year, month, day)}
                                     sx={{
                                         display: "flex", flexDirection: "column",
                                         alignItems: "center", justifyContent: "center",
                                         py: 0.5, borderRadius: 2, cursor: "pointer",
                                         backgroundColor: isSelected ? "rgba(29,158,117,0.1)" : "transparent",
                                         border: isToday ? "1px solid #1D9E75" : "1px solid transparent",
                                         "&:hover": { backgroundColor: "rgba(0,0,0,0.04)" }
                                     }}>
                                    <Typography sx={{
                                        fontSize: 12,
                                        fontWeight: isToday ? 600 : 400,
                                        color: isSelected ? "#1D9E75" : "text.primary"
                                    }}>
                                        {day}
                                    </Typography>
                                    {/* Bulina verde/rosie */}
                                    {calories !== undefined && (
                                        <Box sx={{
                                            width: 6, height: 6, borderRadius: "50%", mt: 0.2,
                                            backgroundColor: calories <= calorieGoal ? "#1D9E75" : "#E53935"
                                        }} />
                                    )}
                                </Box>
                            );
                        })}
                    </Box>
                </CardContent>
            </Card>
        );
    }
}

export default CalendarPanel;