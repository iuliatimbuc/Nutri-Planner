import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Login from './features/Login';
import Home from './features/Home';
import MealDetail from './features/MealDetail';
import WeightProgress from './features/WeightProgress';
import Register from "./features/Register";
import AddFood from "./features/AddFood";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route exact path="/" element={<Login />} />
                <Route path="/login" element={<Login />} />
                <Route path="/home" element={<Home />} />
                <Route path="/meal-detail" element={<MealDetail />} />
                <Route path="/register" element={<Register/>} />
                <Route path="/weight-progress" element={<WeightProgress />} />
                <Route path="/add-food" element={<AddFood/>} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;