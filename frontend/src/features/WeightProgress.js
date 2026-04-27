import React from "react";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import {
    Area, AreaChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer
} from "recharts";
import axiosInstance from "../helper/axios";

class WeightProgress extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            logs: [],
            user: null };
    }

    componentDidMount() {
        this.fetchData();
    }

    fetchData = () => {
        const userId = localStorage.getItem("USER_ID");
        if (userId) {
            axiosInstance.get(`/users/${userId}`)
                .then(res => this.setState({ user: res.data }))
                .catch(err => console.log(err));

            axiosInstance.get(`/weight/${userId}/history`)
                .then(res => this.setState({ logs: res.data }))
                .catch(err => console.log(err));
        }
    }

    render() {
        const { logs, user } = this.state;

        const allWeights = logs.map(l => l.weightNow);
        const minW = allWeights.length > 0 ? Math.floor(Math.min(...allWeights, user?.targetWeight || 0) - 2) : 0;
        const maxW = allWeights.length > 0 ? Math.ceil(Math.max(...allWeights, user?.initialWeight || 0) + 2) : 100;

        const chartData = [
            ...logs.map(log => ({
                date: log.logDate?.slice(5),
                weight: parseFloat(log.weightNow?.toFixed(1)),
            })),
            ...(user?.targetDate && user?.targetWeight ? [{
                date: user.targetDate.slice(5),
                target: user.targetWeight,
            }] : []) // daca user are date si targhet adauga punct
        ];

        return (
            <Container maxWidth="sm" sx={{ pb: 4 }}>

                {/* Header */}
                <Card sx={{ mb: 3, mt: 3 }}>
                    <CardContent sx={{ p: 2, "&:last-child": { pb: 2 } }}>
                        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "flex-end" }}>
                            <Typography variant="h4" fontWeight={500} sx={{ fontSize: 36 }}>
                                Weight Progress
                            </Typography>
                            {user && (
                                <Typography variant="body2" color="text.secondary">
                                    Target: {user.targetDate}
                                </Typography>
                            )}
                        </Box>
                    </CardContent>
                </Card>

                {/* Summary cards */}
                {user && (
                    <Box sx={{ display: "flex", flexDirection: "row", gap: 1.5, mb: 3 }}>
                        {[
                            { label: "Current", val: `${user.weight?.toFixed(1)} kg`, color: "#1D9E75" },
                            { label: "Initial", val: `${user.initialWeight?.toFixed(1)} kg`, color: "#378ADD" },
                            { label: "Target", val: `${user.targetWeight?.toFixed(1)} kg`, color: "#EF9F27" },
                        ].map(s => (
                            <Card key={s.label} sx={{ flex: 1 }}>
                                <CardContent sx={{ p: 2, "&:last-child": { pb: 2 } }}>
                                    <Typography sx={{ fontSize: 20, fontWeight: 600, color: s.color }}>
                                        {s.val}
                                    </Typography>
                                    <Typography variant="caption" color="text.secondary">{s.label}</Typography>
                                </CardContent>
                            </Card>
                        ))}
                    </Box>
                )}

                {/* BMI card */}
                {user && logs.length > 0 && (
                    <Card sx={{ mb: 3 }}>
                        <CardContent sx={{ p: 2, "&:last-child": { pb: 2 } }}>
                            <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                <Box>
                                    <Typography sx={{ fontSize: 20, fontWeight: 600, color: "#9C6FE4" }}>
                                        {logs[logs.length - 1].bmi?.toFixed(1)}
                                    </Typography>
                                    <Typography variant="caption" color="text.secondary" display="block">
                                        Current BMI
                                    </Typography>
                                </Box>
                                <Typography sx={{ fontSize: 13, color: "#9C6FE4", fontWeight: 500 }}>
                                    {(() => {
                                        const bmi = logs[logs.length - 1].bmi;
                                        if (bmi < 18.5) return "Underweight";
                                        if (bmi < 25) return "Normal weight";
                                        if (bmi < 30) return "Overweight";
                                        return "Obese";
                                    })()}
                                </Typography>
                            </Box>
                        </CardContent>
                    </Card>
                )}

                {/* Grafic */}
                {logs.length > 0 && (
                    <Card sx={{ mb: 3 }}>
                        <CardContent sx={{ p: 2 }}>
                            <Typography variant="caption" color="text.secondary"
                                        sx={{ textTransform: "uppercase", letterSpacing: "0.08em", fontWeight: 500, display: "block", mb: 2 }}>
                                Weight Over Time
                            </Typography>
                            <ResponsiveContainer width="100%" height={220}>
                                <AreaChart data={chartData} margin={{ top: 5, right: 10, left: -20, bottom: 0 }}>
                                    <XAxis dataKey="date" tick={{ fontSize: 11 }} axisLine={false} tickLine={false} />
                                    <YAxis domain={[minW, maxW]} tick={{ fontSize: 11 }} axisLine={false} tickLine={false} />
                                    <Tooltip
                                        formatter={(val, name) => [`${val} kg`, name === "weight" ? "Weight" : "Target"]}
                                        contentStyle={{ borderRadius: 8, fontSize: 13 }} />
                                    <Area type="monotone" dataKey="weight"
                                          stroke="#1D9E75" strokeWidth={2.5}
                                          fill="url(#weightGrad)"
                                          dot={{ r: 4, fill: "#1D9E75", strokeWidth: 0 }}
                                          activeDot={{ r: 6 }} />
                                    <Line type="monotone" dataKey="target"
                                          stroke="#EF9F27" strokeWidth={2}
                                          strokeDasharray="5 5"
                                          dot={{ r: 5, fill: "#EF9F27", strokeWidth: 0 }}
                                          connectNulls={false} />
                                </AreaChart>
                            </ResponsiveContainer>
                        </CardContent>
                    </Card>
                )}

                {/* Tabel */}
                {logs.length === 0 ? (
                    <Typography variant="body2" color="text.secondary" textAlign="center" mt={4}>
                        No weight logs yet.
                    </Typography>
                ) : (
                    <TableContainer sx={{ borderRadius: 3, border: "0.5px solid rgba(0,0,0,0.1)" }}>
                        <Table size="small">
                            <TableHead>
                                <TableRow sx={{ backgroundColor: "#F7F7F7" }}>
                                    <TableCell><Typography variant="caption" fontWeight={600}>Date</Typography></TableCell>
                                    <TableCell align="center"><Typography variant="caption" fontWeight={600}>Weight</Typography></TableCell>
                                    <TableCell align="center"><Typography variant="caption" fontWeight={600}>BMI</Typography></TableCell>
                                    <TableCell align="center"><Typography variant="caption" fontWeight={600}>vs Start</Typography></TableCell>
                                    <TableCell align="center"><Typography variant="caption" fontWeight={600}>vs Target</Typography></TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {logs.map((log, index) => (
                                    <TableRow key={log.id}
                                              sx={{ backgroundColor: index % 2 === 0 ? "white" : "#FAFAFA" }}>
                                        <TableCell>
                                            <Typography variant="caption">{log.logDate}</Typography>
                                        </TableCell>
                                        <TableCell align="center">
                                            <Typography variant="caption" fontWeight={500}>
                                                {log.weightNow?.toFixed(1)} kg
                                            </Typography>
                                        </TableCell>
                                        <TableCell align="center">
                                            <Typography variant="caption">{log.bmi?.toFixed(1)}</Typography>
                                        </TableCell>
                                        <TableCell align="center">
                                            <Typography variant="caption">
                                                {(log.weightNow - (user?.initialWeight || 0)) > 0 ? "+" : ""}
                                                {(log.weightNow - (user?.initialWeight || 0)).toFixed(1)} kg
                                            </Typography>
                                        </TableCell>
                                        <TableCell align="center">
                                            <Typography variant="caption">
                                                {log.differenceFromTarget > 0 ? "+" : ""}
                                                {log.differenceFromTarget?.toFixed(1)} kg
                                            </Typography>
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                )}

            </Container>
        );
    }
}

export default WeightProgress;