import React from "react";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import TextField from "@mui/material/TextField";
import InputAdornment from "@mui/material/InputAdornment";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import MenuItem from "@mui/material/MenuItem";
import axiosInstance from "../helper/axios";
import history from "../helper/history";

class AddFood extends React.Component {
    constructor(props) {
        super(props);
        const params = new URLSearchParams(window.location.search);
        const meal = params.get("meal") || "BREAKFAST";
        const date = params.get("date") || new Date().toISOString().slice(0, 10);

        this.state = {
            mealType: meal,
            selectedDate: date,
            allFoods: [],
            searchQuery: "",
            foodDialogOpen: false,
            selectedFood: null,
            selectedGrams: "",
            createFoodOpen: false,
            newFood: {
                name: "", calories: "", protein: "",
                carbs: "", fat: "", servingSize: "", unit: ""
            },
        };
    }

    componentDidMount() {
        axiosInstance.get("/foods")
            .then(res => this.setState({ allFoods: res.data }))
            .catch(err => console.log(err));
    }

    openFoodDialog = (food) => {
        this.setState({ foodDialogOpen: true, selectedFood: food, selectedGrams: food.servingSize || 100 });
    };

    closeFoodDialog = () => {
        this.setState({ foodDialogOpen: false, selectedFood: null, selectedGrams: "" });
    };

    addFoodToMeal = () => {
        const { mealType, selectedFood, selectedGrams, selectedDate } = this.state;
        const userId = localStorage.getItem("USER_ID");

        axiosInstance.post("/logs/save", {
            userId: parseInt(userId),
            foodId: selectedFood.id,
            quantity: parseFloat(selectedGrams),
            mealType: mealType,
            date: selectedDate
        }).then(() => {
            this.closeFoodDialog();
            history.push(`/meal-detail?meal=${mealType}&date=${selectedDate}`);
            window.location.reload();
        }).catch(err => console.log(err));
    };

    handleNewFoodInput = (e) => {
        const { name, value } = e.target;
        this.setState(prev => ({ newFood: { ...prev.newFood, [name]: value } }));
    };

    submitNewFood = () => {
        const { newFood } = this.state;
        if (!newFood.name || !newFood.calories || !newFood.protein || !newFood.carbs || !newFood.fat || !newFood.servingSize || !newFood.unit) {
            alert("Toate campurile sunt obligatorii!");
            return;
        }
        const payload = {
            name: newFood.name,
            calories: parseFloat(newFood.calories) || 0,
            protein: parseFloat(newFood.protein) || 0,
            carbs: parseFloat(newFood.carbs) || 0,
            fat: parseFloat(newFood.fat) || 0,
            servingSize: parseFloat(newFood.servingSize) || 100,
            unit: newFood.unit || "GRAMS"
        };
        axiosInstance.post("/foods/save", payload)
            .then(res => {
                this.setState(prev => ({
                    allFoods: [...prev.allFoods, res.data],
                    createFoodOpen: false,
                    newFood: { name: "", calories: "", protein: "", carbs: "", fat: "", servingSize: "", unit: "" }
                }));
            })
            .catch(err => {
                console.log("eroare:", err);
                alert("Eroare: " + (err.response?.data || err.message));
            });
    };

    getFilteredFoods = () => {
        const q = this.state.searchQuery.toLowerCase();
        return this.state.allFoods.filter(f => f.name.toLowerCase().includes(q));
    };

    getRealTimeNutrition = () => {
        const { selectedFood, selectedGrams } = this.state;
        if (!selectedFood || !selectedGrams) return { calories: 0, protein: 0, carbs: 0, fat: 0 };
        const ratio = parseFloat(selectedGrams) / (selectedFood.servingSize || 100);
        return {
            calories: Math.round(selectedFood.calories * ratio),
            protein: (selectedFood.protein * ratio).toFixed(1),
            carbs: (selectedFood.carbs * ratio).toFixed(1),
            fat: (selectedFood.fat * ratio).toFixed(1),
        };
    };

    getMealLabel = () => {
        const map = { BREAKFAST: "Breakfast", LUNCH: "Lunch", DINNER: "Dinner", SNACK: "Snack" };
        return map[this.state.mealType] || this.state.mealType;
    };

    render() {
        const { searchQuery, foodDialogOpen, selectedFood, selectedGrams,
            createFoodOpen, newFood, selectedDate } = this.state;

        const filteredFoods = this.getFilteredFoods();
        const nutrition = this.getRealTimeNutrition();

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
                                Add to {this.getMealLabel()}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                {selectedDate}
                            </Typography>
                        </Box>
                    </CardContent>
                </Card>

                {/* Search */}
                <TextField fullWidth variant="outlined" size="small" placeholder="Search food..."
                           value={searchQuery}
                           onChange={e => this.setState({ searchQuery: e.target.value })}
                           sx={{ mb: 1.5 }}
                           InputProps={{
                               startAdornment: <InputAdornment position="start">🔍</InputAdornment>
                           }} />

                {/* Create button */}
                <Button fullWidth variant="outlined"
                        onClick={() => this.setState({ createFoodOpen: true })}
                        sx={{ borderRadius: 2, textTransform: "none", fontSize: 13, mb: 3 }}>
                    + Create Food
                </Button>

                {/* Food List */}
                <Typography variant="caption" color="text.secondary"
                            sx={{ textTransform: "uppercase", letterSpacing: "0.08em", fontWeight: 500, display: "block", mb: 1 }}>
                    Foods
                </Typography>

                {filteredFoods.length === 0 ? (
                    <Typography variant="body2" color="text.secondary" textAlign="center" mt={4}>
                        No foods found.
                    </Typography>
                ) : (
                    filteredFoods.map(food => (
                        <Card key={food.id} sx={{ ...card, cursor: "pointer" }}
                              onClick={() => this.openFoodDialog(food)}>
                            <CardContent sx={{ p: 2, "&:last-child": { pb: 2 } }}>
                                <Box display="flex" justifyContent="space-between" alignItems="center">
                                    <Box>
                                        <Typography fontWeight={500} fontSize={15}>{food.name}</Typography>
                                        <Typography variant="caption" color="text.secondary">
                                            {food.servingSize}{food.unit === "GRAMS" ? "g" :
                                            food.unit === "MILLILITERS" ? "ml" :
                                                food.unit === "PIECE" ? " piece" : " portion"} serving
                                        </Typography>
                                    </Box>
                                    <Typography fontWeight={500} fontSize={14} sx={{ color: "#1D9E75" }}>
                                        {food.calories} kcal
                                    </Typography>
                                </Box>
                            </CardContent>
                        </Card>
                    ))
                )}

                {/* Food Detail Dialog */}
                <Dialog open={foodDialogOpen} onClose={this.closeFoodDialog}
                        fullWidth maxWidth="xs" PaperProps={{ sx: { borderRadius: 4 } }}>
                    {selectedFood && (
                        <>
                            <DialogTitle>
                                <Typography variant="h6" fontWeight={600}>{selectedFood.name}</Typography>
                            </DialogTitle>
                            <DialogContent>
                                <Box display="flex" justifyContent="space-around" mt={1} mb={3}
                                     sx={{ backgroundColor: "#F7F7F7", borderRadius: 3, p: 2 }}>
                                    {[
                                        { label: "Calories", val: nutrition.calories, color: "#1D9E75" },
                                        { label: "Protein",  val: `${nutrition.protein}g`, color: "#1D9E75" },
                                        { label: "Carbs",    val: `${nutrition.carbs}g`,   color: "#378ADD" },
                                        { label: "Fat",      val: `${nutrition.fat}g`,     color: "#EF9F27" },
                                    ].map(n => (
                                        <Box key={n.label} textAlign="center">
                                            <Typography sx={{ fontSize: 18, fontWeight: 600, color: n.color }}>
                                                {n.val}
                                            </Typography>
                                            <Typography variant="caption" color="text.secondary">{n.label}</Typography>
                                        </Box>
                                    ))}
                                </Box>
                                <TextField fullWidth variant="outlined" label="Quantity"
                                           type="number" value={selectedGrams}
                                           onChange={e => this.setState({ selectedGrams: e.target.value })}
                                           InputProps={{
                                               endAdornment: (
                                                   <InputAdornment position="end">
                                                       {selectedFood.unit === "GRAMS" ? "g" :
                                                           selectedFood.unit === "MILLILITERS" ? "ml" :
                                                               selectedFood.unit === "PIECE" ? "pcs" : "portion"}
                                                   </InputAdornment>
                                               )
                                           }} />
                                <Typography variant="caption" color="text.secondary" mt={1} display="block">
                                    Standard serving: {selectedFood.servingSize}
                                    {selectedFood.unit === "GRAMS" ? "g" :
                                        selectedFood.unit === "MILLILITERS" ? "ml" :
                                            selectedFood.unit === "PIECE" ? " piece" : " portion"}
                                </Typography>
                            </DialogContent>
                            <DialogActions sx={{ px: 3, pb: 3 }}>
                                <Button onClick={this.closeFoodDialog}
                                        sx={{ borderRadius: 2, textTransform: "none" }}>
                                    Cancel
                                </Button>
                                <Button variant="outlined" onClick={this.addFoodToMeal}
                                        sx={{ borderRadius: 2, textTransform: "none", px: 3 }}>
                                    Add to {this.getMealLabel()}
                                </Button>
                            </DialogActions>
                        </>
                    )}
                </Dialog>

                {/* Create Food Dialog */}
                <Dialog open={createFoodOpen}
                        onClose={() => this.setState({ createFoodOpen: false })}
                        fullWidth maxWidth="xs" PaperProps={{ sx: { borderRadius: 4 } }}>
                    <DialogTitle>Create Food</DialogTitle>
                    <DialogContent>
                        <Box mt={1}>
                            <TextField label="Name" name="name" fullWidth size="small" required
                                       value={newFood.name} onChange={this.handleNewFoodInput}
                                       sx={{ mb: 2.5 }} />
                            <TextField label="Serving Size" name="servingSize" fullWidth size="small" required
                                       type="number" value={newFood.servingSize} onChange={this.handleNewFoodInput}
                                       sx={{ mb: 2.5 }} />
                            <TextField select label="Unit" name="unit" fullWidth size="small" required
                                       value={newFood.unit} onChange={this.handleNewFoodInput}
                                       sx={{ mb: 2.5 }}>
                                <MenuItem value="GRAMS">Grams</MenuItem>
                                <MenuItem value="MILLILITERS">Milliliters</MenuItem>
                                <MenuItem value="PIECE">Piece</MenuItem>
                                <MenuItem value="PORTION">Portion</MenuItem>
                            </TextField>
                            <TextField label="Calories (kcal)" name="calories" fullWidth size="small" required
                                       type="number" value={newFood.calories} onChange={this.handleNewFoodInput}
                                       sx={{ mb: 2.5 }} />
                            <TextField label="Protein (g)" name="protein" fullWidth size="small" required
                                       type="number" value={newFood.protein} onChange={this.handleNewFoodInput}
                                       sx={{ mb: 2.5 }} />
                            <TextField label="Carbs (g)" name="carbs" fullWidth size="small" required
                                       type="number" value={newFood.carbs} onChange={this.handleNewFoodInput}
                                       sx={{ mb: 2.5 }} />
                            <TextField label="Fat (g)" name="fat" fullWidth size="small" required
                                       type="number" value={newFood.fat} onChange={this.handleNewFoodInput} />
                        </Box>
                    </DialogContent>
                    <DialogActions sx={{ px: 3, pb: 3 }}>
                        <Button onClick={() => this.setState({ createFoodOpen: false })}
                                sx={{ borderRadius: 2, textTransform: "none" }}>
                            Cancel
                        </Button>
                        <Button variant="outlined" onClick={this.submitNewFood}
                                sx={{ borderRadius: 2, textTransform: "none", px: 3 }}>
                            Create
                        </Button>
                    </DialogActions>
                </Dialog>

            </Container>
        );
    }
}

export default AddFood;