import type { JSX } from "react";
import { Navigate } from "react-router-dom";

function isTokenValid(token: string | null) {
    if (!token) return false;

    try {
        const payload = JSON.parse(atob(token.split(".")[1]));
        return Date.now() < payload.exp * 1000;
    } catch {
        return false;
    }
}

function ProtectedRoute({ children }: { children: JSX.Element }) {
    const token = localStorage.getItem("token");

    if (!isTokenValid(token)) {
        return <Navigate to="/register" replace />;
    }

    return children;
}

export default ProtectedRoute;