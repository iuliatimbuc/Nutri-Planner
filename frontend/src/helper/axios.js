import axios from "axios";

const axiosInstance = axios.create({
    headers: {
        post: {
            "Content-Type": "application/json",
        }
    }
});

axiosInstance.interceptors.request.use(config => {
    if (config.url.includes("/auth/")) {
        config.baseURL = "http://localhost:8082"; // auth
    } else {
        config.baseURL = "http://localhost:8080"; // nutri-planner
    }
    return config;
});

export default axiosInstance;