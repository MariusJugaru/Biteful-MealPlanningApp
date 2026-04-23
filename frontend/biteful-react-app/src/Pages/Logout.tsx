import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function Logout() {
    const navigate = useNavigate();

    useEffect(() => {
        localStorage.removeItem("token"); // mai corect decât setItem("")
        navigate("/register");
    }, [navigate]);

  return null;
}

export default Logout;