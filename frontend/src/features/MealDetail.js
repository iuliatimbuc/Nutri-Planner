import React from "react";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import TextField from "@mui/material/TextField";
import InputAdornment from "@mui/material/InputAdornment";
import axiosInstance from "../helper/axios";
import history from "../helper/history";

class MealDetail extends React.Component {
    constructor(props) {
        super(props);
        const params = new URLSearchParams(window.location.search);
        const meal = params.get("meal") || "BREAKFAST";
        const date = params.get("date") || new Date().toISOString().slice(0, 10);

        this.state = {
            mealType: meal,
            selectedDate: date,
            logs: [],
            totalCalories: 0,
            totalProtein: 0,
            totalCarbs: 0,
            totalFat: 0,
            editDialogOpen: false,
            editLog: null,
            editQuantity: "",
            deleteDialogOpen: false,
            deleteLogId: null,
        };
    }

    componentDidMount() {
        this.fetchLogs();
    }

    fetchLogs = () => {
        const userId = localStorage.getItem("USER_ID");
        const { mealType, selectedDate } = this.state;

        axiosInstance.get(`/logs/${userId}?date=${selectedDate}`)
            .then(res => {
                const mealKey = mealType.toLowerCase();
                const logsKey = `${mealKey}Logs`;
                const logs = res.data[logsKey] || [];

                const totalCalories = logs.reduce((sum, l) => sum + (l.calculatedCalories || 0), 0);
                const totalProtein = logs.reduce((sum, l) => sum + (l.calculatedProtein || 0), 0);
                const totalCarbs = logs.reduce((sum, l) => sum + (l.calculatedCarbs || 0), 0);
                const totalFat = logs.reduce((sum, l) => sum + (l.calculatedFat || 0), 0);

                this.setState({ logs, totalCalories, totalProtein, totalCarbs, totalFat });
            })
            .catch(err => console.log(err));
    };

    getMealLabel = () => {
        const map = { BREAKFAST: "Breakfast", LUNCH: "Lunch", DINNER: "Dinner", SNACK: "Snack" };
        return map[this.state.mealType] || this.state.mealType;
    };

    confirmDelete = (logId) => {
        this.setState({ deleteDialogOpen: true, deleteLogId: logId });
    };

    deleteLog = () => {
        const { deleteLogId } = this.state;
        axiosInstance.delete(`/logs/delete/${deleteLogId}`)
            .then(() => {
                this.setState({ deleteDialogOpen: false, deleteLogId: null });
                this.fetchLogs();
            })
            .catch(err => console.log(err));
    };

    openEdit = (log) => {
        this.setState({ editDialogOpen: true, editLog: log, editQuantity: log.quantity });
    };

    closeEdit = () => {
        this.setState({ editDialogOpen: false, editLog: null, editQuantity: "" });
    };

    saveEdit = () => {
        const { editLog, editQuantity, mealType } = this.state;
        axiosInstance.put(`/logs/update`, {
            logId: editLog.id,
            quantity: parseFloat(editQuantity),
            mealType: mealType
        }).then(() => {
            this.closeEdit();
            this.fetchLogs();
        }).catch(err => console.log(err));
    };

    render() {
        const { logs, totalCalories, totalProtein, totalCarbs, totalFat,
            editDialogOpen, editLog, editQuantity,
            deleteDialogOpen, selectedDate } = this.state;

        const card = {
            borderRadius: "16px",
            border: "0.5px solid rgba(0,0,0,0.1)",
            boxShadow: "none",
            mb: 1.5
        };

        return (
            <Container maxWidth="sm" sx={{ pb: 4 }}>

                {/* Header */}
                <Card sx={{ mb: 3, mt: 3 }}>
                    <CardContent sx={{ p: 2, "&:last-child": { pb: 2 } }}>
                        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "flex-end" }}>
                            <Typography variant="h4" fontWeight={500} sx={{ fontSize: 36 }}>
                                {this.getMealLabel()}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                {selectedDate}
                            </Typography>
                        </Box>
                    </CardContent>
                </Card>

                {/* Totals summary cards */}
                <Box sx={{ display: "flex", flexDirection: "row", gap: 1.5, mb: 3 }}>
                    {[
                        { label: "Calories", val: `${totalCalories.toFixed(0)} kcal`, color: "#1D9E75" },
                        { label: "Protein",  val: `${totalProtein.toFixed(1)}g`,       color: "#1D9E75" },
                        { label: "Carbs",    val: `${totalCarbs.toFixed(1)}g`,         color: "#378ADD" },
                        { label: "Fat",      val: `${totalFat.toFixed(1)}g`,           color: "#EF9F27" },
                    ].map(s => (
                        <Card key={s.label} sx={{ flex: 1 }}>
                            <CardContent sx={{ p: 2, "&:last-child": { pb: 2 } }}>
                                <Typography sx={{ fontSize: 15, fontWeight: 600, color: s.color }}>
                                    {s.val}
                                </Typography>
                                <Typography variant="caption" color="text.secondary">{s.label}</Typography>
                            </CardContent>
                        </Card>
                    ))}
                </Box>

                {/* Logs list */}
                <Typography variant="caption" color="text.secondary"
                            sx={{ textTransform: "uppercase", letterSpacing: "0.08em", fontWeight: 500, display: "block", mb: 1 }}>
                    Foods
                </Typography>

                {logs.length === 0 ? (
                    <Typography variant="body2" color="text.secondary" textAlign="center" mt={4}>
                        No foods logged yet.
                    </Typography>
                ) : (
                    logs.map(log => (
                        <Card key={log.id} sx={card}>
                            <CardContent sx={{ p: 2, "&:last-child": { pb: 2 } }}>
                                {/* Title */}
                                <Typography fontWeight={500} fontSize={15} mb={1.5}>
                                    {log.food?.name}
                                </Typography>

                                {/* Colored macro mini-cards including quantity */}
                                <Box sx={{ display: "flex", flexDirection: "row", gap: 1, mb: 1.5 }}>
                                    {[
                                        { label: "Quantity",  val: `${log.quantity}g`,                          color: "#9C6FE4" },
                                        { label: "Calories",  val: `${log.calculatedCalories?.toFixed(0)} kcal`, color: "#1D9E75" },
                                        { label: "Protein",   val: `${log.calculatedProtein?.toFixed(1)}g`,      color: "#1D9E75" },
                                        { label: "Carbs",     val: `${log.calculatedCarbs?.toFixed(1)}g`,        color: "#378ADD" },
                                        { label: "Fat",       val: `${log.calculatedFat?.toFixed(1)}g`,          color: "#EF9F27" },
                                    ].map(m => (
                                        <Box key={m.label} flex={1} sx={{
                                            backgroundColor: "#F7F7F7",
                                            borderRadius: 2, p: 1, textAlign: "center"
                                        }}>
                                            <Typography sx={{ fontSize: 12, fontWeight: 600, color: m.color, lineHeight: 1.2 }}>
                                                {m.val}
                                            </Typography>
                                            <Typography variant="caption" color="text.secondary" sx={{ fontSize: 10 }}>
                                                {m.label}
                                            </Typography>
                                        </Box>
                                    ))}
                                </Box>

                                {/* Edit / Delete buttons */}
                                <Box sx={{ display: "flex", flexDirection: "row", gap: 1 }}>
                                    <Button variant="outlined"
                                            onClick={() => this.openEdit(log)}
                                            sx={{ flex: 1, borderRadius: 2, textTransform: "none", fontSize: 12 }}>
                                        Edit
                                    </Button>
                                    <Button variant="outlined" color="error"
                                            onClick={() => this.confirmDelete(log.id)}
                                            sx={{ flex: 1, borderRadius: 2, textTransform: "none", fontSize: 12 }}>
                                        Delete
                                    </Button>
                                </Box>
                            </CardContent>
                        </Card>
                    ))
                )}

                {/* Add food button */}
                <Box display="flex" gap={1} mt={2}>
                    <Button fullWidth variant="outlined"
                            onClick={() => {
                                history.push(`/add-food?meal=${this.state.mealType}&date=${selectedDate}`);
                                window.location.reload();
                            }}
                            sx={{ borderRadius: 2, textTransform: "none", fontSize: 13 }}>
                        + Add Food
                    </Button>
                </Box>

                {/* Edit Dialog */}
                <Dialog open={editDialogOpen} onClose={this.closeEdit}
                        fullWidth maxWidth="xs" PaperProps={{ sx: { borderRadius: 4 } }}>
                    <DialogTitle>Edit Quantity</DialogTitle>
                    <DialogContent>
                        <Typography variant="body2" color="text.secondary" mb={2}>
                            {editLog?.food?.name}
                        </Typography>
                        <TextField fullWidth variant="outlined" label="Quantity (g)"
                                   type="number" value={editQuantity}
                                   onChange={e => this.setState({ editQuantity: e.target.value })}
                                   InputProps={{
                                       endAdornment: <InputAdornment position="end">g</InputAdornment>
                                   }} />
                    </DialogContent>
                    <DialogActions sx={{ px: 3, pb: 3 }}>
                        <Button onClick={this.closeEdit}
                                sx={{ borderRadius: 2, textTransform: "none" }}>
                            Cancel
                        </Button>
                        <Button variant="outlined" onClick={this.saveEdit}
                                sx={{ borderRadius: 2, textTransform: "none", px: 3 }}>
                            Save
                        </Button>
                    </DialogActions>
                </Dialog>

                {/* Delete Confirm Dialog */}
                <Dialog open={deleteDialogOpen}
                        onClose={() => this.setState({ deleteDialogOpen: false })}
                        fullWidth maxWidth="xs" PaperProps={{ sx: { borderRadius: 4 } }}>
                    <DialogTitle>Delete Food</DialogTitle>
                    <DialogContent>
                        <Typography variant="body2">
                            Esti sigur ca vrei sa stergi acest aliment din jurnal?
                        </Typography>
                    </DialogContent>
                    <DialogActions sx={{ px: 3, pb: 3 }}>
                        <Button onClick={() => this.setState({ deleteDialogOpen: false })}
                                sx={{ borderRadius: 2, textTransform: "none" }}>
                            Cancel
                        </Button>
                        <Button variant="outlined" color="error" onClick={this.deleteLog}
                                sx={{ borderRadius: 2, textTransform: "none", px: 3 }}>
                            Delete
                        </Button>
                    </DialogActions>
                </Dialog>

            </Container>
        );
    }
}

export default MealDetail;