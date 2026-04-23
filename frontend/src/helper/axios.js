import axios from "axios";

const axiosInstance = axios.create({
    headers: {
        post: {
            "Content-Type": "application/json",
        }
    }
});

axiosInstance.interceptors.request.use(config => {
    const path = window.location.pathname;
    if (path.includes("login")) {
        config.baseURL = "http://localhost:8082";
    } else {
        config.baseURL = "http://localhost:8080";
    }
    return config;
});

export default axiosInstance;