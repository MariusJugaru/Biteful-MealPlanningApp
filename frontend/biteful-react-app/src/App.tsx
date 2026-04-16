import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useState } from "react";

// Pages
import Home from "./Pages/Home";
import Recipes from "./Pages/Recipes";
import Saved from "./Pages/Saved";
import SavedRecipe from "./Pages/SavedRecipe";


function App() {
  localStorage.setItem("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0NGFiMTViZS0yNzBlLTRiMDctODIzZS1kOTA3N2E0YjUzMDEiLCJ1c2VybmFtZSI6InRlc3QiLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc3NjM0MjQ4OCwiZXhwIjoxNzc2MzQ2MDg4fQ.X6E2bpwmE6x5X9vIWLUAouKeQomSFfjTveElPvac4uE");

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/recipes" element={<Recipes />} />
        <Route path="/saved" element={<Saved />} />
        <Route path="/saved/:id" element={<SavedRecipe />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;